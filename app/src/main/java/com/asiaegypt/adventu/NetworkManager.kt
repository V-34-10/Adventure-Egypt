package com.asiaegypt.adventu

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

object NetworkManager {

    @SuppressLint("ObsoleteSdkInt")
    fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && (activeNetworkInfo.isConnected || activeNetworkInfo.isConnectedOrConnecting)
    }
}