package com.example.firstgame

import java.text.SimpleDateFormat
import java.util.*

//This class represents a single item in the scoreboard.
//It has three properties: the player's score, the time it took to achieve that score, and the date the score was achieved.
//This data class is a simple and convenient way to store and pass around information about a scoreboard item.
data class ScoreboardItem(
    val name: String,
    val score: Int,
    val date: Date
) {
    fun formatDate(): String {
        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}

