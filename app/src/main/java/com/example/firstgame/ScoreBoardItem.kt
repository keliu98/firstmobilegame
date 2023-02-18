package com.example.firstgame

import java.util.*

data class ScoreboardItem(val name: String, val score: Int, val date: Date) {
    companion object {
        fun fromScore(name: String, score: Score): ScoreboardItem {
            return ScoreboardItem(name, score.score, score.date)
        }
    }
}

data class Score(val score: Int, val date: Date)
