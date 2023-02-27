package com.example.firstgame

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database where everything is stored.
 */
@Database(entities = [ScoreboardItem::class], version = 1, exportSchema = false)
abstract class ScoreBoardDatabase : RoomDatabase() {

    abstract fun ScoreBoardDAO(): ScoreBoardDAO

    companion object {
        @Volatile
        private var INSTANCE: ScoreBoardDatabase? = null

        /**
         * Gets Database. If there is non, make one. Else, return the database
         * that is currently there.
         */
        fun getDatabase(context: Context): ScoreBoardDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreBoardDatabase::class.java,
                    "scoreboard_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}