package com.asiaegypt.advente.ui.ads

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.appodeal.ads.InterstitialCallbacks

object AdsSection {
    var interstitialShowed = false
    fun loadAds(activity: AppCompatActivity) {
        Appodeal.setInterstitialCallbacks(object : InterstitialCallbacks {
            override fun onInterstitialLoaded(isPrecache: Boolean) {
                if (!interstitialShowed) {
                    Appodeal.show(activity, Appodeal.INTERSTITIAL)
                }
            }

            override fun onInterstitialFailedToLoad() {
                Log.d(AdsSection.toString(), "onInterstitialFailedToLoad")
            }

            override fun onInterstitialShown() {
                interstitialShowed = true
            }

            override fun onInterstitialShowFailed() {
                Log.d(AdsSection.toString(), "onInterstitialShowFailed")
            }

            override fun onInterstitialClosed() {
                Log.d(AdsSection.toString(), "onInterstitialClosed")
                //interstitialShowed = false
            }

            override fun onInterstitialClicked() {}

            override fun onInterstitialExpired() {
                //Appodeal.cache(activity, Appodeal.INTERSTITIAL)
                Log.d(AdsSection.toString(), "onInterstitialExpired")
            }
        })
    }
}