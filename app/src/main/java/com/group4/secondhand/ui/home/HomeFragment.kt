package com.group4.secondhand.ui.home

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.data.model.ResponseGetProduct
import com.group4.secondhand.databinding.FragmentHomeBinding
import com.group4.secondhand.ui.home.paging.PagingViewModel
import com.group4.secondhand.ui.home.paging.ProductPagingAdapter
import com.group4.secondhand.ui.home.paging.ProductStateAdapter
import com.group4.secondhand.ui.home.paging.UiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : Fragment() {
    companion object {
        var result = 0
        const val PRODUCT_ID = "id"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val pagingViewModel : PagingViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        binding.statusBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, result
        )
        getBanner()
        getCategory()
        changeToolbar()
        val  productAdapter = ProductPagingAdapter {
            val productBundle = Bundle()
                productBundle.putInt(PRODUCT_ID, it.id)
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, productBundle)

        }
        val productStateAdapter = ProductStateAdapter {

            productAdapter.retry()
        }
        binding.refreshContainer.setOnRefreshListener{
            setUpPaging(productAdapter,productStateAdapter,pagingViewModel.getProducts())
        }
        setUpPaging(productAdapter,productStateAdapter,pagingViewModel.getProducts())

        binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        categoryAdapter = CategoryAdapter(object : CategoryAdapter.OnClickListener {
            override fun onClickItem(data: ResponseCategoryHome) {
                Toast.makeText(
                    requireContext(),
                    "Menampilkan kategori ${data.name}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                setUpPaging(productAdapter,productStateAdapter,pagingViewModel.getProducts(data.id))
            }
        })
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun changeToolbar() {
        var toolbarColored = false
        var toolbarTransparent = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val bannerHeight =
                    (binding.imgBanner.height / 2) - result - binding.statusBar.height
                val colored = ContextCompat.getColor(requireContext(), R.color.medium_blue)
                val transparent =
                    ContextCompat.getColor(requireContext(), android.R.color.transparent)

                when {
                    scrollY > bannerHeight -> {
                        if (toolbarTransparent) {
                            val colorAnimate =
                                ValueAnimator.ofObject(ArgbEvaluator(), transparent, colored)
                            colorAnimate.addUpdateListener {
                                binding.statusBar.setBackgroundColor(it.animatedValue as Int)
                                binding.toolbar.setBackgroundColor(it.animatedValue as Int)
                            }
                            colorAnimate.duration = 250
                            colorAnimate.start()
                            toolbarColored = true
                            toolbarTransparent = false
                        }
                    }
                    else -> {
                        if (toolbarColored) {
                            val colorAnimate =
                                ValueAnimator.ofObject(ArgbEvaluator(), colored, transparent)
                            colorAnimate.addUpdateListener {
                                binding.statusBar.setBackgroundColor(it.animatedValue as Int)
                                binding.toolbar.setBackgroundColor(it.animatedValue as Int)
                            }
                            colorAnimate.duration = 250
                            colorAnimate.start()
                            toolbarColored = false
                            toolbarTransparent = true
                        }
                    }
                }
            }
        }
    }

    private fun getBanner() {
        homeViewModel.banner.observe(viewLifecycleOwner) { banner ->
            if (banner.status == Status.SUCCESS) {
                val imageSlider = binding.imgBanner
                val bannerA = arrayListOf<SlideModel>()
                banner.data?.forEach {
                    bannerA.add(SlideModel(it.imageUrl))
                }
                imageSlider.setImageList(bannerA, ScaleTypes.FIT)
            }
        }
        homeViewModel.getBannerHome()
    }

    private fun getCategory() {
        homeViewModel.getCategoryHome()
        homeViewModel.category.observe(viewLifecycleOwner) { category ->
            if (category.status == Status.SUCCESS) {
                binding.shimmerCategory.visibility = View.GONE

                categoryAdapter.submitData(category.data)
                binding.rvCategory.adapter = categoryAdapter
            }
        }
    }
    private fun setUpPaging(
        adapter: ProductPagingAdapter,
        load: ProductStateAdapter,
        pagingData: Flow<PagingData<UiModel.ProductItem>>
    ) {
        val footerAdapter = ProductStateAdapter { adapter.retry() }
        binding.rvProduct.adapter = adapter.withLoadStateHeaderAndFooter(
            header = load,
            footer = footerAdapter
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.refreshContainer.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        val gridLayoutManager = binding.rvProduct.layoutManager as GridLayoutManager
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if ((position == adapter.itemCount && footerAdapter.itemCount > 0) ||
                    (position == adapter.itemCount && load.itemCount > 0)
                ) {
                    2
                } else {
                    1
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            pagingData.collectLatest(adapter::submitData)
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                load.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && adapter.itemCount > 0 }
                    ?: loadState.prepend
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.rvProduct.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading
                binding.pbProduct.isVisible = loadState.mediator?.refresh is LoadState.Loading
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        context,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}
