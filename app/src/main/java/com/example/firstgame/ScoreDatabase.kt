package com.example.firstgame

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class ScoreDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "score.db"
        const val TABLE_NAME = "score"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SCORE = "score"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_SCORE INTEGER,"
                + "$COLUMN_DATE INTEGER)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not implemented for this example
    }

    fun addScore(score: ScoreboardItem) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, score.name)
            put(COLUMN_SCORE, score.score)
            put(COLUMN_DATE, score.date.time)
        }
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun getTopScores(limit: Int): List<ScoreboardItem> {
        val scores = mutableListOf<ScoreboardItem>()
        val cursor = readableDatabase.query(
            TABLE_NAME,
            arrayOf(COLUMN_NAME, COLUMN_SCORE, COLUMN_DATE),
            null,
            null,
            null,
            null,
            "$COLUMN_SCORE DESC",
            limit.toString()
        )
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val score = getInt(getColumnIndexOrThrow(COLUMN_SCORE))
                val date = Date(getLong(getColumnIndexOrThrow(COLUMN_DATE)))
                val item = ScoreboardItem(name, score, date)
                scores.add(item)
            }
        }
        return scores
    }

    fun clearAllScores() {
        writableDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }
}
