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
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.adapter.PairAdapter
import com.asiaegypt.adventu.databinding.FragmentActecBinding
import com.asiaegypt.adventu.databinding.FragmentAsianBinding
import com.asiaegypt.adventu.databinding.FragmentEgyptBinding
import com.asiaegypt.adventu.model.Pairs
import com.asiaegypt.adventu.model.ScoreStats
import com.asiaegypt.adventu.ui.score.ScoreManager

object ManagerFindPair {
    private lateinit var preferences: SharedPreferences
    private lateinit var recyclerViewSceneGame: RecyclerView

    private lateinit var adapter: PairAdapter
    private val pairList = mutableListOf<Pairs>()

    private var firstPair: Pairs? = null
    private var secondPair: Pairs? = null
    private var flipping = false
    private val imageList = mutableListOf<Int>()

    private var selectedLevel: String = ""
    private var selectedTheme: String = ""
    private var stepsCount = 0

    val stats = mutableMapOf(
        "Easy" to ScoreStats(),
        "Medium" to ScoreStats(),
        "Hard" to ScoreStats()
    )

    private var timer: CountDownTimer? = null
    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    fun initFindPairGame(binding: ViewBinding, context: Context) {
        preferences =
            context.getSharedPreferences("AsianEgyptAdventurePref", AppCompatActivity.MODE_PRIVATE)
        selectedLevel = preferences.getString("levelFindPair", "").toString()
        selectedTheme = preferences.getString("themeFindPair", "").toString()
        val spanCount = getSpanCountLevel()
        adapter = PairAdapter(pairList, selectedLevel, selectedTheme)

        when (binding) {
            is FragmentActecBinding -> {
                recyclerViewSceneGame = binding.sceneGame
            }

            is FragmentEgyptBinding -> {
                recyclerViewSceneGame = binding.sceneGame
            }

            is FragmentAsianBinding -> {
                recyclerViewSceneGame = binding.sceneGame
            }
        }

        recyclerViewSceneGame.layoutManager = GridLayoutManager(context, spanCount)
        recyclerViewSceneGame.adapter = adapter

        insertPairItems()

        adapter.onPairClick = { cardItem, position ->
            handlePairClick(cardItem, position)
        }
    }

    private fun getSpanCountLevel(): Int {
        return when (selectedLevel) {
            "Easy" -> 3
            "Medium" -> 4
            "Hard" -> 5
            else -> 3
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun insertPairItems() {
        val numPairs = when (selectedLevel) {
            "Easy" -> 4 // 4 пари для easy (8 карток)
            "Medium" -> 8 // 8 пар для medium (16 карток)
            "Hard" -> 12 // 12 пар для hard (24 карток)
            else -> 4
        }

        imageList.clear()
        imageList.addAll(choiceImagesForLevelAndThemes(selectedLevel))

        pairList.clear()
        var position = 0
        for (i in 0 until numPairs) {
            val imageRes = imageList[i]
            pairList.add(Pairs(imageRes, pos = position++))
            pairList.add(Pairs(imageRes, pos = position++))
        }

        if (selectedLevel == "Easy") {
            pairList.add(Pairs(imageList[0], pos = position++))
        }

        pairList.shuffle()
        adapter.notifyDataSetChanged()
    }

    private fun choiceImagesForLevelAndThemes(level: String): List<Int> {
        val baseImages = when (selectedTheme) {
            "Actec" -> {
                when (level) {
                    "Easy" -> listOf(
                        R.drawable.pair_actec_easy_1,
                        R.drawable.pair_actec_easy_2,
                        R.drawable.pair_actec_easy_3
                    )

                    "Medium" -> listOf(
                        R.drawable.pair_actec_medium_1,
                        R.drawable.pair_actec_medium_2,
                        R.drawable.pair_actec_medium_3,
                        R.drawable.pair_actec_medium_4
                    )

                    "Hard" -> listOf(
                        R.drawable.pair_actec_hard_1,
                        R.drawable.pair_actec_hard_2,
                        R.drawable.pair_actec_hard_3,
                        R.drawable.pair_actec_hard_4,
                        R.drawable.pair_actec_hard_5
                    )

                    else -> listOf(
                        R.drawable.pair_actec_easy_1,
                        R.drawable.pair_actec_easy_2,
                        R.drawable.pair_actec_easy_3
                    )
                }
            }

            "Egypt" -> {
                when (level) {
                    "Easy" -> listOf(
                        R.drawable.pair_egypt_easy_1,
                        R.drawable.pair_egypt_easy_2,
                        R.drawable.pair_egypt_easy_3
                    )

                    "Medium" -> listOf(
                        R.drawable.pair_egypt_medium_1,
                        R.drawable.pair_egypt_medium_2,
                        R.drawable.pair_egypt_medium_3,
                        R.drawable.pair_egypt_medium_4
                    )

                    "Hard" -> listOf(
                        R.drawable.pair_egypt_hard_1,
                        R.drawable.pair_egypt_hard_2,
                        R.drawable.pair_egypt_hard_3,
                        R.drawable.pair_egypt_hard_4,
                        R.drawable.pair_egypt_hard_5
                    )

                    else -> listOf(
                        R.drawable.pair_egypt_easy_1,
                        R.drawable.pair_egypt_easy_2,
                        R.drawable.pair_egypt_easy_3
                    )
                }
            }

            "Asian" -> {
                when (level) {
                    "Easy" -> listOf(
                        R.drawable.pair_asian_easy_1,
                        R.drawable.pair_asian_easy_2,
                        R.drawable.pair_asian_easy_3
                    )

                    "Medium" -> listOf(
                        R.drawable.pair_asian_medium_1,
                        R.drawable.pair_asian_medium_2,
                        R.drawable.pair_asian_medium_3,
                        R.drawable.pair_asian_medium_4
                    )

                    "Hard" -> listOf(
                        R.drawable.pair_asian_hard_1,
                        R.drawable.pair_asian_hard_2,
                        R.drawable.pair_asian_hard_3,
                        R.drawable.pair_asian_hard_4,
                        R.drawable.pair_asian_hard_5
                    )

                    else -> listOf(
                        R.drawable.pair_asian_easy_1,
                        R.drawable.pair_asian_easy_2,
                        R.drawable.pair_asian_easy_3
                    )
                }
            }

            else -> {
                when (level) {
                    "Easy" -> listOf(
                        R.drawable.pair_actec_easy_1,
                        R.drawable.pair_actec_easy_2,
                        R.drawable.pair_actec_easy_3
                    )

                    "Medium" -> listOf(
                        R.drawable.pair_actec_medium_1,
                        R.drawable.pair_actec_medium_2,
                        R.drawable.pair_actec_medium_3,
                        R.drawable.pair_actec_medium_4
                    )

                    "Hard" -> listOf(
                        R.drawable.pair_actec_hard_1,
                        R.drawable.pair_actec_hard_2,
                        R.drawable.pair_actec_hard_3,
                        R.drawable.pair_actec_hard_4,
                        R.drawable.pair_actec_hard_5
                    )

                    else -> listOf(
                        R.drawable.pair_actec_easy_1,
                        R.drawable.pair_actec_easy_2,
                        R.drawable.pair_actec_easy_3
                    )
                }
            }
        }

        return when (level) {
            "Easy" -> baseImages + baseImages.take(4) // 9 карток
            "Medium" -> baseImages + baseImages + baseImages.take(3) // 16 карток
            "Hard" -> baseImages + baseImages + baseImages + baseImages + baseImages.take(4) // 25 карток
            else -> baseImages + baseImages.take(4) // 9 карток
        }
    }

    private fun handlePairClick(pairItem: Pairs, position: Int) {
        if (flipping || pairItem.flipped || pairItem.matched) return

        pairItem.flipped = true
        pairItem.pos = position
        adapter.notifyItemChanged(position)

        if (firstPair == null) {
            firstPair = pairItem
            startTimer()
        } else {
            secondPair = pairItem
            flipping = true

            Handler(Looper.getMainLooper()).postDelayed({
                checkMatchPair()
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
        stepsCount++
        adapter.notifyItemChanged(firstPos)
        adapter.notifyItemChanged(secondPos)
        firstPair = null
        secondPair = null
        flipping = false

        if (checkGameOver()) {
            recordGameStats()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetFindPairGame() {
        firstPair = null
        secondPair = null
        flipping = false

        pairList.shuffle()

        pairList.forEach { pair ->
            pair.flipped = false
            pair.matched = false
        }

        adapter.notifyDataSetChanged()
        stepsCount = 0
    }

    private fun recordGameStats() {
        val levelStats = stats[selectedLevel]!!

        /*levelStats.gamesPlayed++
        if (stepsCount <= 20) {
            levelStats.wins++
        } else {
            levelStats.losses++
        }*/

        if (elapsedTime < levelStats.bestTime) {
            levelStats.bestTime = elapsedTime
        }
        if (stepsCount < levelStats.bestSteps || levelStats.bestSteps == 0) {
            levelStats.bestSteps = stepsCount
        }

        ScoreManager.saveStatsScoreFindPairGame(preferences)
        stopTimer()
        stepsCount = 0
    }

    private fun checkGameOver(): Boolean {
        return if ((selectedLevel == "Easy") || (selectedLevel == "Hard")) {
            pairList.count { it.matched } == pairList.size - 1
        } else {
            pairList.all { it.matched }
        }
    }
}