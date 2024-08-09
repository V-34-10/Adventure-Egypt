package com.asiaegypt.adventu.ui.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.asiaegypt.adventu.adapter.PairAdapter
import com.asiaegypt.adventu.databinding.FragmentActecBinding
import com.asiaegypt.adventu.databinding.FragmentAsianBinding
import com.asiaegypt.adventu.databinding.FragmentEgyptBinding
import com.asiaegypt.adventu.model.Pairs
import com.asiaegypt.adventu.ui.score.ScoreManager
import com.asiaegypt.adventu.ui.score.ScoreManager.stats

object ManagerFindPair {
    private lateinit var preferences: SharedPreferences
    private lateinit var recyclerViewSceneGame: RecyclerView

    private lateinit var adapterPair: PairAdapter
    private val pairList = mutableListOf<Pairs>()

    private var firstPair: Pairs? = null
    private var secondPair: Pairs? = null
    private var flippingPair = false
    private val imageListPair = mutableListOf<Int>()

    private var selectedLevelPairGame: String = ""
    private var selectedThemePairGame: String = ""
    private var stepSearchPair = 0

    private var timer: CountDownTimer? = null
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var gameStarted = false

    fun initFindPairGame(binding: ViewBinding, context: Context) {
        preferences =
            context.getSharedPreferences("AsianEgyptAdventurePref", AppCompatActivity.MODE_PRIVATE)
        selectedLevelPairGame = preferences.getString("levelFindPair", "").toString()
        selectedThemePairGame = preferences.getString("themeFindPair", "").toString()
        val spanCount = getSpanCountLevel()
        adapterPair = PairAdapter(pairList, selectedLevelPairGame, selectedThemePairGame)

        recyclerViewSceneGame = when (binding) {
            is FragmentActecBinding -> binding.sceneGame
            is FragmentEgyptBinding -> binding.sceneGame
            is FragmentAsianBinding -> binding.sceneGame
            else -> throw IllegalArgumentException("Unsupported binding type")
        }

        recyclerViewSceneGame.layoutManager = GridLayoutManager(context, spanCount)
        recyclerViewSceneGame.adapter = adapterPair

        insertPairItems()

        adapterPair.onPairClick = { cardItem, position ->
            handlePairClick(cardItem, position, binding)
        }
    }

    private fun getSpanCountLevel(): Int = when (selectedLevelPairGame) {
        "Easy" -> 3
        "Medium" -> 4
        "Hard" -> 5
        else -> 3
    }

    private fun getPairsDependedLevel(): Int = when (selectedLevelPairGame) {
        "Easy" -> 4
        "Medium" -> 8
        "Hard" -> 12
        else -> 4
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun insertPairItems() {
        imageListPair.clear()
        imageListPair.addAll(ImageProvider.getImagesForLevelAndTheme(selectedLevelPairGame, selectedThemePairGame))

        pairList.clear()
        var position = 0
        for (i in 0 until getPairsDependedLevel()) {
            val imageRes = imageListPair[i]
            pairList.add(Pairs(imageRes, pos = position++))
            pairList.add(Pairs(imageRes, pos = position++))
        }

        if ((selectedLevelPairGame == "Easy") || (selectedLevelPairGame == "Hard")) {
            pairList.add(Pairs(imageListPair[0], pos = position++))
        }

        pairList.shuffle()
        adapterPair.notifyDataSetChanged()
    }

    private fun handlePairClick(pairItem: Pairs, position: Int, binding: ViewBinding) {
        if (flippingPair || pairItem.flipped || pairItem.matched) return

        if (!gameStarted) {
            startTimer()
            gameStarted = true
        }

        pairItem.flipped = true
        pairItem.pos = position
        adapterPair.notifyItemChanged(position)

        if (firstPair == null) {
            firstPair = pairItem
        } else {
            secondPair = pairItem
            flippingPair = true

            Handler(Looper.getMainLooper()).postDelayed({
                checkMatchPair()
                updateTextStepPair(binding)
            }, 1000)
        }
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime = System.currentTimeMillis() - startTime
            }

            override fun onFinish() {}
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
        startTime = 0
        elapsedTime = 0
    }

    private fun checkMatchPair() {
        val firstPos = firstPair?.pos ?: -1
        val secondPos = secondPair?.pos ?: -1

        if (firstPair?.image == secondPair?.image) {
            firstPair?.matched = true
            secondPair?.matched = true
        } else {
            firstPair?.flipped = false
            secondPair?.flipped = false
        }
        stepSearchPair++
        adapterPair.notifyItemChanged(firstPos)
        adapterPair.notifyItemChanged(secondPos)
        firstPair = null
        secondPair = null
        flippingPair = false

        if (checkGameOver()) {
            recordGameStats()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetFindPairGame(binding: ViewBinding) {
        firstPair = null
        secondPair = null
        flippingPair = false

        pairList.shuffle()

        pairList.forEach { pair ->
            pair.flipped = false
            pair.matched = false
        }

        adapterPair.notifyDataSetChanged()
        stepSearchPair = 0
        updateTextStepPair(binding)
        gameStarted = false
        stopTimer()
    }

    private fun updateTextStepPair(binding: ViewBinding) {
        when (binding) {
            is FragmentActecBinding -> binding.textSteps.text = stepSearchPair.toString()
            is FragmentEgyptBinding -> binding.textSteps.text = stepSearchPair.toString()
            is FragmentAsianBinding -> binding.textSteps.text = stepSearchPair.toString()
        }
    }

    private fun recordGameStats() {
        val levelStats = stats[selectedLevelPairGame]!!

        if (elapsedTime < levelStats.bestTime) {
            levelStats.bestTime = elapsedTime
        }
        if (stepSearchPair < levelStats.bestSteps || levelStats.bestSteps == 0) {
            levelStats.bestSteps = stepSearchPair
        }

        ScoreManager.saveStatsScoreFindPairGame(preferences)
        stepSearchPair = 0
    }

    private fun checkGameOver(): Boolean {
        return if ((selectedLevelPairGame == "Easy") || (selectedLevelPairGame == "Hard")) {
            pairList.count { it.matched } == pairList.size - 1
        } else {
            pairList.all { it.matched }
        }
    }
}