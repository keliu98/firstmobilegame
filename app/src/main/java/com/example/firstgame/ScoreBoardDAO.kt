package com.example.firstgame

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data access object (DAO)
 * must be interface or abstract class
 * data source for these methods are entity objects
 *
 * These are kind of Android's way of translating kotlin code to
 * SQL commands.
 */

@Dao
interface ScoreBoardDAO {
    @Insert
    fun insert(scoreboardItem: ScoreboardItem)

    @Query("SELECT * FROM ScoreBoard ORDER BY uid DESC LIMIT 1") //This is an SQl command
    fun getLastScoreItem(): LiveData<ScoreboardItem>

    @Query("SELECT * FROM ScoreBoard ORDER BY uid DESC")
    fun getAllScoreItems(): LiveData<List<ScoreboardItem>>

    @Query("DELETE FROM ScoreBoard")
    fun deleteAll()
}