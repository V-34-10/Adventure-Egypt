package com.asiaegypt.adventu.model

data class Pairs(
    val image: Int,
    var flipped: Boolean = false,
    var matched: Boolean = false,
    var pos: Int = -1
)