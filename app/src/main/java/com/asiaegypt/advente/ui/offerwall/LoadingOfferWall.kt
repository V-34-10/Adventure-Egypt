package com.asiaegypt.advente.ui.offerwall

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.asiaegypt.advente.ui.offerwall.model.Game
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class LoadingOfferWall {
    private var linkJSON: String =
        "https://zettaplayz.com/mainMenu"
    private lateinit var cookies: String
    private var call: Call? = null
    private var isTimeUp = false
    private var latch = CountDownLatch(1)
    fun fetchInterstitialData(callback: (Result<List<Game>>) -> Unit) {
        latch = CountDownLatch(1)
        val client = OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(linkJSON)
            .build()

        call = client.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(Result.Failure(e))
                latch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback(Result.Failure(IOException("Unexpected code $response")))
                        latch.countDown()
                        return
                    }
                    cookies = response.header("Set-Cookie") ?: ""
                    try {
                        val json = response.body?.string()
                        Log.d("LoadingOfferWall", "Raw JSON response: $json")
                        val games = parseJson(json)
                        callback(Result.Success(games))
                    } catch (e: Exception) {
                        Log.e("LoadingOfferWall", "Error parsing JSON: ${e.message}", e)
                        callback(Result.Failure(e))
                    }
                }
                latch.countDown()
            }
        })
        if (!latch.await(10, TimeUnit.SECONDS)) {
            isTimeUp = true
            call?.cancel()
            callback(Result.Failure(Exception("Timeout")))
        }
    }


    private fun parseJson(json: String?): List<Game> {
        return try {
            if (json.isNullOrEmpty()) {
                Log.w("LoadingOfferWall", "JSON is null or empty. Returning empty list.")
                emptyList()
            } else {
                val gson = Gson()
                gson.fromJson(json, Array<Game>::class.java).toList()
            }
        } catch (e: JsonSyntaxException) {
            Log.e("LoadingOfferWall", "Error parsing JSON: ${e.message}", e)
            emptyList()
        }
    }


    fun cancel() {
        call?.cancel()
    }

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Failure(val exception: Exception) : Result<Nothing>()
    }
}