package com.group4.secondhand.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.group4.secondhand.R
import com.group4.secondhand.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainer)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}