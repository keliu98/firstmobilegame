package com.example.firstgame

import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

//This class represents a single item in the scoreboard.
//It has three properties: the player's score, the time it took to achieve that score, and the date the score was achieved.
//This data class is a simple and convenient way to store and pass around information about a scoreboard item.

@Entity(tableName = "ScoreBoard")
data class ScoreboardItem(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    val name: String,
    val score: Int,
    //Store as a string to prevent future complications. Basic Data Types are always easier.
    val date: String,
) {
    //Removed for now as it is up to user to convert Date to string.

//    fun formatDate(): String {
//        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
//        return formatter.format(date)
//    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////



