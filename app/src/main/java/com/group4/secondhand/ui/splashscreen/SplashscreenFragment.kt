package com.group4.secondhand.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.ui.MainActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashscreenFragment : Fragment() {

    companion object{
        const val SHARED_PREF = "kotlinsharedpreferences"
        const val ONBOARDING_PREF = "onBoarding"
        const val TOKEN_PREF = "token"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splashscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val showOnBoarding = sharedPreferences.getBoolean(ONBOARDING_PREF, true)
        Handler().postDelayed({
            if (showOnBoarding){
                findNavController().navigate(R.id.action_splashscreenFragment_to_firstOnBoardingFragment)
            } else {
                activity?.let{
                    val intent = Intent (it, MainActivity::class.java)
                    it.startActivity(intent)
                }
                activity?.finish()
            }
        }, 3000)
    }
}