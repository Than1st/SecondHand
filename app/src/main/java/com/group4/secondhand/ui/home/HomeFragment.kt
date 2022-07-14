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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.data.model.ResponseGetProduct
import com.group4.secondhand.data.model.ResponseNotification
import com.group4.secondhand.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

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
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private var categorySelected = ""


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
        getCategory()
        changeToolbar()
        detailProduct()
        fetchProduct(categorySelected)
        binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
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

    private fun getCategory() {
        homeViewModel.getCategoryHome()
        homeViewModel.category.observe(viewLifecycleOwner) { category ->
            if (category.status == Status.SUCCESS) {
                binding.shimmerCategory.visibility = View.GONE
                categoryAdapter = CategoryAdapter(object : CategoryAdapter.OnClickListener {
                    override fun onClickItem(data: ResponseCategoryHome) {
                        Toast.makeText(
                            requireContext(),
                            "Menampilkan kategori ${data.name}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        categorySelected = data.id.toString()
                        fetchProduct(categorySelected)
                    }
                })
                categoryAdapter.submitData(category.data)
                binding.rvCategory.adapter = categoryAdapter
            }
        }
    }

    private fun fetchProduct(categoryId: String) {
        val status = "available"
        homeViewModel.product.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.shimmer.visibility = View.VISIBLE
                    binding.lottieEmpty.visibility = View.GONE
                    binding.tvEmptyProduct.visibility = View.GONE
                    binding.rvProduct.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.shimmer.visibility = View.GONE
                    if (it.data?.size == 0) {
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.tvEmptyProduct.visibility = View.VISIBLE
                    }
                    binding.rvProduct.visibility = View.VISIBLE
                    productAdapter.submitData(it.data)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        homeViewModel.getProduct(status, categoryId,"","","")
    }

    private fun detailProduct() {
        productAdapter = ProductAdapter(object : ProductAdapter.OnClickListener {
            override fun onClickItem(data: ResponseGetProduct) {
                val productBundle = Bundle()
                productBundle.putInt(PRODUCT_ID, data.id)
                Handler().postDelayed({
                    findNavController().navigate(
                        R.id.action_homeFragment_to_detailFragment,
                        productBundle
                    )
                }, 1500)
            }
        })
        binding.rvProduct.adapter = productAdapter
    }

}