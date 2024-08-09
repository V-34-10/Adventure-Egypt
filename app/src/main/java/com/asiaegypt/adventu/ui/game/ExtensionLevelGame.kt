package com.asiaegypt.adventu.ui.game

fun String.getSpanCount(): Int = when (this) {
    "Easy" -> 3
    "Medium" -> 4
    "Hard" -> 5
    else -> 3
}

fun String.getNumPairs(): Int = when (this) {
    "Easy" -> 4
    "Medium" -> 8
    "Hard" -> 12
    else -> 4
}