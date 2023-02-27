package com.example.firstgame

import androidx.lifecycle.LiveData

class ScoreBoardRepository (private val scoreBoardDAO : ScoreBoardDAO) {

    val allData: LiveData<List<ScoreboardItem>> = scoreBoardDAO.getAllScoreItems()
    val lastScoreItem: LiveData<ScoreboardItem> = scoreBoardDAO.getLastScoreItem()

    /**
     * Calls DAO's insert function.
     */
     fun insert (scoreItem: ScoreboardItem) {
        scoreBoardDAO.insert(scoreItem)
    }

    /**
     * Calls DAO's deleteAll function
     */
     fun deleteAll () {
        scoreBoardDAO.deleteAll()
    }
}