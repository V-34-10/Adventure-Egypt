package com.asiaegypt.adventu.ui.game.managers

import com.asiaegypt.adventu.R

object ImageProvider {

    fun getImagesForLevelAndTheme(level: String, theme: String): List<Int> {
        val baseImages = getImagesByTheme(theme, level)
        val repetitions = getRepetitionsForLevel(level)
        return baseImages.repeatAndAppend(repetitions)
    }

    private fun getImagesByTheme(theme: String, level: String): List<Int> {
        return when (theme) {
            "Actec" -> getActecImages(level)
            "Egypt" -> getEgyptImages(level)
            "Asian" -> getAsianImages(level)
            else -> getActecImages(level)
        }
    }

    private fun getRepetitionsForLevel(level: String): Pair<Int, Int> {
        return when (level) {
            "Easy" -> 1 to 4
            "Medium" -> 2 to 3
            "Hard" -> 5 to 4
            else -> 1 to 4
        }
    }

    private fun List<Int>.repeatAndAppend(repetitions: Pair<Int, Int>): List<Int> {
        val (times, extra) = repetitions
        return this.repeat(times) + this.take(extra)
    }

    private fun List<Int>.repeat(times: Int): List<Int> {
        return List(times) { this }.flatten()
    }

    private fun getActecImages(level: String): List<Int> {
        return when (level) {
            "Easy" -> listOf(
                R.drawable.pair_actec_easy_1,
                R.drawable.pair_actec_easy_2,
                R.drawable.pair_actec_easy_3,
                R.drawable.pair_actec_medium_2
            )
            "Medium" -> listOf(
                R.drawable.pair_actec_medium_1,
                R.drawable.pair_actec_medium_2,
                R.drawable.pair_actec_medium_3,
                R.drawable.pair_actec_medium_4,
                R.drawable.pair_actec_hard_1,
                R.drawable.pair_actec_hard_2,
                R.drawable.pair_actec_hard_3,
                R.drawable.pair_actec_hard_4
            )
            "Hard" -> listOf(
                R.drawable.pair_actec_hard_1,
                R.drawable.pair_actec_hard_2,
                R.drawable.pair_actec_hard_3,
                R.drawable.pair_actec_hard_4,
                R.drawable.pair_actec_hard_5,
                R.drawable.pair_actec_medium_1,
                R.drawable.pair_actec_medium_2,
                R.drawable.pair_actec_medium_3,
                R.drawable.pair_actec_medium_4,
                R.drawable.pair_actec_easy_1,
                R.drawable.pair_actec_easy_2,
                R.drawable.pair_actec_easy_3
            )
            else -> listOf(
                R.drawable.pair_actec_easy_1,
                R.drawable.pair_actec_easy_2,
                R.drawable.pair_actec_easy_3,
                R.drawable.pair_actec_medium_2
            )
        }
    }

    private fun getEgyptImages(level: String): List<Int> {
        return when (level) {
            "Easy" -> listOf(
                R.drawable.pair_egypt_easy_1,
                R.drawable.pair_egypt_easy_2,
                R.drawable.pair_egypt_easy_3,
                R.drawable.pair_egypt_medium_1
            )

            "Medium" -> listOf(
                R.drawable.pair_egypt_medium_1,
                R.drawable.pair_egypt_medium_2,
                R.drawable.pair_egypt_medium_3,
                R.drawable.pair_egypt_medium_4,
                R.drawable.pair_egypt_hard_1,
                R.drawable.pair_egypt_hard_2,
                R.drawable.pair_egypt_hard_3,
                R.drawable.pair_egypt_hard_4
            )

            "Hard" -> listOf(
                R.drawable.pair_egypt_hard_1,
                R.drawable.pair_egypt_hard_2,
                R.drawable.pair_egypt_hard_3,
                R.drawable.pair_egypt_hard_4,
                R.drawable.pair_egypt_hard_5,
                R.drawable.pair_egypt_medium_1,
                R.drawable.pair_egypt_medium_2,
                R.drawable.pair_egypt_medium_3,
                R.drawable.pair_egypt_medium_4,
                R.drawable.pair_egypt_easy_1,
                R.drawable.pair_egypt_easy_2,
                R.drawable.pair_egypt_easy_3
            )

            else -> listOf(
                R.drawable.pair_egypt_easy_1,
                R.drawable.pair_egypt_easy_2,
                R.drawable.pair_egypt_easy_3,
                R.drawable.pair_egypt_medium_1
            )
        }
    }

    private fun getAsianImages(level: String): List<Int> {
        return when (level) {
            "Easy" -> listOf(
                R.drawable.pair_asian_easy_1,
                R.drawable.pair_asian_easy_2,
                R.drawable.pair_asian_easy_3,
                R.drawable.pair_asian_medium_1
            )

            "Medium" -> listOf(
                R.drawable.pair_asian_medium_1,
                R.drawable.pair_asian_medium_2,
                R.drawable.pair_asian_medium_3,
                R.drawable.pair_asian_medium_4,
                R.drawable.pair_asian_hard_1,
                R.drawable.pair_asian_hard_2,
                R.drawable.pair_asian_hard_3,
                R.drawable.pair_asian_hard_4
            )

            "Hard" -> listOf(
                R.drawable.pair_asian_hard_1,
                R.drawable.pair_asian_hard_2,
                R.drawable.pair_asian_hard_3,
                R.drawable.pair_asian_hard_4,
                R.drawable.pair_asian_hard_5,
                R.drawable.pair_asian_medium_1,
                R.drawable.pair_asian_medium_2,
                R.drawable.pair_asian_medium_3,
                R.drawable.pair_asian_medium_4,
                R.drawable.pair_asian_easy_1,
                R.drawable.pair_asian_easy_2,
                R.drawable.pair_asian_easy_3
            )

            else -> listOf(
                R.drawable.pair_asian_easy_1,
                R.drawable.pair_asian_easy_2,
                R.drawable.pair_asian_easy_3,
                R.drawable.pair_asian_medium_1
            )
        }
    }
}