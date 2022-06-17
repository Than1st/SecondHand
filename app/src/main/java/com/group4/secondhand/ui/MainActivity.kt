package com.group4.secondhand.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.group4.secondhand.R
import com.group4.secondhand.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreen()
        val navController = findNavController(R.id.fragmentContainer)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.akunFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                R.id.loginCompose -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                R.id.registerCompose -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
            }
        }

    }
    private fun fullscreen() {
        setFullScreen(window)
        lightStatusBar(window)
    }
}