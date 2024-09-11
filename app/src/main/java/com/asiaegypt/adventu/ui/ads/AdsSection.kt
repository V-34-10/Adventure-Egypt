package com.asiaegypt.adventu.ui.ads

import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.appodeal.ads.InterstitialCallbacks
import com.asiaegypt.adventu.ui.splash.MainActivity

object AdsSection {
    private var interstitialShowed = false
    fun loadAds(activity: AppCompatActivity) {
        Appodeal.setInterstitialCallbacks(object : InterstitialCallbacks {
            override fun onInterstitialLoaded(isPrecache: Boolean) {
                if (!interstitialShowed) {
                    Appodeal.show(activity, Appodeal.INTERSTITIAL)
                }
            }

            override fun onInterstitialFailedToLoad() {
                (activity as MainActivity).startToMenu()
            }

            override fun onInterstitialShown() {
                interstitialShowed = true
            }

            override fun onInterstitialShowFailed() {
                (activity as MainActivity).startToMenu()
            }

            override fun onInterstitialClosed() {
                (activity as MainActivity).startToMenu()
            }

            override fun onInterstitialClicked() {}

            override fun onInterstitialExpired() {
                Appodeal.cache(activity, Appodeal.INTERSTITIAL)
            }
        })
    }
}