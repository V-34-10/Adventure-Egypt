package com.asiaegypt.adventu.ui.game

import com.asiaegypt.adventu.R

object ImageProvider {

    fun getImagesForLevelAndTheme(level: String, theme: String): List<Int> {
        val baseImages = when (theme) {
            "Actec" -> getActecImages(level)
            "Egypt" -> getEgyptImages(level)
            "Asian" -> getAsianImages(level)
            else -> getActecImages(level)
        }

        return when (level) {
            "Easy" -> baseImages + baseImages.take(4)
            "Medium" -> baseImages + baseImages + baseImages.take(3)
            "Hard" -> baseImages + baseImages + baseImages + baseImages + baseImages.take(4)
            else -> baseImages + baseImages.take(4)
        }
    }

    private fun getActecImages(level: String): List<Int> {
        return when (level) {
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

    private fun getEgyptImages(level: String): List<Int> {
        return when (level) {
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

    private fun getAsianImages(level: String): List<Int> {
        return when (level) {
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
}