package com.group4.secondhand.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.group4.secondhand.R
import com.group4.secondhand.databinding.ActivityMainBinding

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
            if(destination.id == R.id.registerCompose || destination.id == R.id.loginCompose) {

                binding.bottomNavigation.visibility = View.GONE
            } else {

                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

    }


    fun fullscreen() {
        setFullScreen(window)
        lightStatusBar(window)
    }
}
