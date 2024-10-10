package com.asiaegypt.advente

import android.app.Application
import com.appodeal.ads.Appodeal

class App : Application() {
    private val key = "82c0486f9352d0aaf342730701298b43f9ac3280de9b429a"
    override fun onCreate() {
        super.onCreate()
        Appodeal.initialize(this, key, Appodeal.INTERSTITIAL)
    }
}