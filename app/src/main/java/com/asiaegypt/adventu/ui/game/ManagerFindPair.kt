package com.asiaegypt.adventu.ui.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.adapter.PairAdapter
import com.asiaegypt.adventu.databinding.FragmentActecBinding
import com.asiaegypt.adventu.databinding.FragmentAsianBinding
import com.asiaegypt.adventu.databinding.FragmentEgyptBinding
import com.asiaegypt.adventu.model.Pairs
import com.asiaegypt.adventu.ui.score.ScoreManager
import com.asiaegypt.adventu.ui.score.ScoreManager.stats

object ManagerFindPair {
    private var delay = 1000L
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
        preferences = GameSettings.getPreference(context)

        selectedLevelPairGame = GameSettings.selectedLevel
        selectedThemePairGame = GameSettings.selectedTheme

        adapterPair = PairAdapter(pairList, selectedLevelPairGame, selectedThemePairGame)

        recyclerViewSceneGame = when (binding) {
            is FragmentActecBinding -> binding.sceneGame
            is FragmentEgyptBinding -> binding.sceneGame
            is FragmentAsianBinding -> binding.sceneGame
            else -> throw IllegalArgumentException("Unsupported binding type")
        }

        recyclerViewSceneGame.layoutManager =
            GridLayoutManager(context, selectedLevelPairGame.getSpanCount())
        recyclerViewSceneGame.adapter = adapterPair

        insertPairItems()

        adapterPair.onPairClick = { cardItem, position ->
            handlePairClick(cardItem, position, binding, context)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun insertPairItems() {
        var position = 0
        imageListPair.clear()
        imageListPair.addAll(
            ImageProvider.getImagesForLevelAndTheme(
                selectedLevelPairGame,
                selectedThemePairGame
            )
        )

        pairList.clear()
        for (i in 0 until selectedLevelPairGame.getNumPairs()) {
            val imageRes = imageListPair[i]
            pairList.add(Pairs(imageRes, pos = position++))
            pairList.add(Pairs(imageRes, pos = position++))
        }

        if (selectedLevelPairGame in listOf("Easy", "Hard")) {
            pairList.add(Pairs(imageListPair[0], pos = position++))
        }

        pairList.shuffle()
        adapterPair.notifyDataSetChanged()
    }

    private fun handlePairClick(pairItem: Pairs, position: Int, binding: ViewBinding, context: Context) {
        if (flippingPair || pairItem.flipped || pairItem.matched) return

        if (!gameStarted) {
            startTimer()
            gameStarted = true
        }

        pairItem.flipped = true
        pairItem.pos = position
        adapterPair.notifyItemChanged(position)

        when (firstPair) {
            null -> firstPair = pairItem
            else -> {
                secondPair = pairItem
                flippingPair = true
                Handler(Looper.getMainLooper()).postDelayed({
                    checkMatchPair(context)
                    updateTextStepPair(binding)
                }, delay)
            }
        }
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        timer = object : CountDownTimer(Long.MAX_VALUE, delay) {
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

    private fun checkMatchPair(context: Context) {
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
            saveBestStatsFindGame()
            showWinToast(context)
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

    private fun showWinToast(context: Context) {
        Toast.makeText(context, R.string.toast_win, Toast.LENGTH_SHORT).show()
    }

    private fun saveBestStatsFindGame() {
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
        return when (selectedLevelPairGame) {
            "Easy", "Hard" -> pairList.count { it.matched } == pairList.size - 1
            else -> pairList.all { it.matched }
        }
    }
}