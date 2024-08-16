package com.asiaegypt.adventu.ui.ads

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import com.asiaegypt.adventu.databinding.ActivityMainBinding
import com.asiaegypt.adventu.ui.menu.MenuActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class AdsSection(
    private val activity: Activity,
    private val binding: ActivityMainBinding
) : AdsService {

    private var linkAds: String = "https://on.kabushinoko.com/interstitial"
    private var preferences: SharedPreferences =
        activity.getSharedPreferences("AsianEgyptAdventurePref", Context.MODE_PRIVATE)

    private lateinit var cookiesAds: String
    private lateinit var userAgentAds: String

    private var callAds: Call? = null
    private var isTimeOverResponse = false
    private var latchTime = CountDownLatch(1)
    override fun fetchAndLoadAds() {
        val client = createClient()
        val request = createRequest(linkAds)
        callAds = client.newCall(request)
        callAds?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = handleFailure(call, e)
            override fun onResponse(call: Call, response: Response) = handleResponse(call, response)
        })
        checkTimeOut()
    }

    private fun createClient(): OkHttpClient =
        OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build()

    private fun createRequest(url: String): Request = Request.Builder().url(url).build()

    private fun handleFailure(call: Call, e: IOException) {
        if (call.isCanceled()) {
            Log.i("AdsSection", "Call was cancelled")
        } else {
            e.printStackTrace()
        }
        latchTime.countDown()
    }

    private fun handleResponse(call: Call, response: Response) {
        if (call.isCanceled()) {
            Log.i("AdsSection", "Call was cancelled")
        } else {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                cookiesAds = response.header("Set-Cookie") ?: ""
                userAgentAds = response.header("User-Agent") ?: ""
                response.body?.string()?.let { json ->
                    parseJsonAndLoadImage(json, cookiesAds, userAgentAds)
                }
            }
        }
        latchTime.countDown()
    }

    private fun checkTimeOut() {
        if (!latchTime.await(10, TimeUnit.SECONDS)) {
            isTimeOverResponse = true
            callAds?.cancel()
            loadingMenuApp()
        }
    }

    override fun loadingMenuApp() {
        activity.runOnUiThread {
            activity.startActivity(Intent(activity, MenuActivity::class.java))
        }
        activity.finish()
    }

    override fun cancel() {
        if (callAds?.isCanceled() == false) callAds?.cancel()
    }

    private fun parseJsonAndLoadImage(json: String, cookies: String?, userAgent: String?) {
        try {
            val jsonObject = JSONObject(json)
            val actionUrl = jsonObject.getString("action")
            val sourceUrl = jsonObject.getString("source")

            saveAdsData(actionUrl, sourceUrl, cookies, userAgent)
            loadImageAds(sourceUrl, actionUrl, cookies, userAgent)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun saveAdsData(
        actionUrl: String,
        sourceUrl: String,
        cookies: String?,
        userAgent: String?
    ) {
        preferences.edit().apply {
            putString("actionUrl", actionUrl)
            putString("sourceUrl", sourceUrl)
            putString("cookies", cookies)
            putString("userAgent", userAgent)
            apply()
        }
    }

    override fun loadImageAds(
        imageUrl: String,
        actionUrl: String,
        cookies: String?,
        userAgent: String?
    ) {
        val client = createClient()
        val request = createRequest(imageUrl)
            .newBuilder()
            .header("Cookie", cookies.toString())
            .header("User-Agent", userAgent.toString())
            .build()
        callAds = client.newCall(request)
        callAds?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = handleFailure(call, e)
            override fun onResponse(call: Call, response: Response) =
                handleImageResponse(response, actionUrl)
        })
        checkTimeOut()
    }

    private fun handleImageResponse(response: Response, actionUrl: String) {
        response.use {
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val bitmap = response.body?.byteStream()?.let { BitmapFactory.decodeStream(it) }
            activity.runOnUiThread {
                if (!isTimeOverResponse && bitmap != null) {
                    binding.resBanner.visibility = View.VISIBLE
                    binding.resBanner.setImageBitmap(bitmap)
                    binding.resBanner.setOnClickListener {
                        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(actionUrl)))
                    }
                }
            }
        }
        latchTime.countDown()
    }
}