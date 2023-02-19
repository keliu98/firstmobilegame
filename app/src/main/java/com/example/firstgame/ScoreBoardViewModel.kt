package com.example.firstgame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData



class ScoreBoardViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: ScoreBoardRepository
    val allData: LiveData<List<ScoreboardItem>>
    val lastScoreItem: LiveData<ScoreboardItem>

    /**
     * A handy way to init without needing a constructor
     */
    init {
        val scoreDAO = ScoreBoardDatabase
            .getDatabase(application).ScoreBoardDAO()
        repository = ScoreBoardRepository(scoreDAO)
        lastScoreItem = repository.lastScoreItem
        allData = repository.allData
    }

    /**
     * Calls the insert function in repository
     */
    fun insert(scoreItem: ScoreboardItem) {
        repository.insert(scoreItem)
    }

    /**
     * Calls the delete all function in repository
     */
    fun deleteAllScores() {
        repository.deleteAll()
    }
}