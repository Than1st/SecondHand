package com.group4.secondhand.ui.home

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    companion object {
        var result = 0
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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
    }

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
            when (category.status) {
                Status.SUCCESS -> {
                    val adapter = CategoryAdapter(object:CategoryAdapter.OnClickListener{
                        override fun onClickItem(data: ResponseCategoryHome) {
                            Toast.makeText(requireContext(), "${data.name}", Toast.LENGTH_SHORT).show()
                        }
                    })
                    adapter.submitData(category.data)
                    binding.rvCategory.adapter = adapter

                }
            }
        }
    }


}