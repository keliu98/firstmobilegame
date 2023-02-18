package com.example.firstgame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.util.*


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