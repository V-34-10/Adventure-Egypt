package com.asiaegypt.adventu.ui.ads

import android.app.Application
import com.appodeal.ads.Appodeal

class App : Application() {
    private val key = "65f9db0a7329cccc8036b208245e59175aa2abb180a54960"
    override fun onCreate() {
        super.onCreate()
        Appodeal.initialize(this, key, Appodeal.INTERSTITIAL)
    }
}