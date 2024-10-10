package com.asiaegypt.advente

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View

object NavigationManager {

    private fun shouldHideNavigationBar(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val config = context.resources.configuration
            config.navigation == Configuration.NAVIGATION_NONAV
        } else {
            false
        }
    }

    fun handleNavigationBarVisibility(context: Context) {
        if (shouldHideNavigationBar(context)) {
            (context as Activity).window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
}