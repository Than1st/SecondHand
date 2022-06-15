package com.group4.secondhand.ui

import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

fun setFullScreen(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun lightStatusBar(window: Window, light: Boolean = true) {
    val wic = WindowInsetsControllerCompat(window,window.decorView)
    wic.isAppearanceLightStatusBars = light
}