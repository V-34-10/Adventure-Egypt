package com.asiaegypt.adventu.ui.ads

interface AdsService {
    fun fetchAndLoadAds()
    fun loadImageAds(
        imageUrl: String,
        actionUrl: String,
        cookies: String?,
        userAgent: String?
    )

    fun cancel()
    fun loadingMenuApp()
}