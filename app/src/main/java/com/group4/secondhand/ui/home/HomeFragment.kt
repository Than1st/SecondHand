package com.group4.secondhand.ui.home

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.group4.secondhand.R
import com.group4.secondhand.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
//        view.setOnApplyWindowInsetsListener { v, insets ->
//            view.height = insets.systemWindowInsetTop
//        }
//        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
//        val checkOnboarding = sharedPreferences.getBoolean(ONBOARDING_PREF, false)
//        if (checkOnboarding){
//            Toast.makeText(requireContext(), "Halo!", Toast.LENGTH_SHORT).show()
//        }
        binding.statusBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight())
        changeToolbar()
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun changeToolbar(){
        var toolbarColored = false
        var toolbarTransparent = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            binding.scrollView.setOnScrollChangeListener {_,_,scrollY,_,_->
                val bannerHeight = (binding.imgBanner.height /2) - getStatusBarHeight() - binding.statusBar.height
                val colored = ContextCompat.getColor(requireContext(), R.color.medium_blue)
                val transparent = ContextCompat.getColor(requireContext(), android.R.color.transparent)

                when {
                    scrollY > bannerHeight ->{
                        if (toolbarTransparent){
                            val colorAnimate = ValueAnimator.ofObject(ArgbEvaluator(),transparent,colored)
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
                    else ->{
                        if(toolbarColored){
                            val colorAnimate = ValueAnimator.ofObject(ArgbEvaluator(),colored,transparent)
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


}