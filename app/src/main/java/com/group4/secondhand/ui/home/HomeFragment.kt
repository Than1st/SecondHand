package com.group4.secondhand.ui.home

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.group4.secondhand.databinding.FragmentHomeBinding
import com.group4.secondhand.ui.splashscreen.SplashscreenFragment.Companion.ONBOARDING_PREF
import com.group4.secondhand.ui.splashscreen.SplashscreenFragment.Companion.SHARED_PREF


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

    }

}