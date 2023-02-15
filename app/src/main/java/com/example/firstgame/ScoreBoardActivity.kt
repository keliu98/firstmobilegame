package com.example.firstgame

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ScoreBoardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreboardAdapter: ScoreBoardAdapter
    private lateinit var scoresList: MutableList<ScoreboardItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        // Initialize scores list
        scoresList = mutableListOf()
        for (i in 1..20) {
            val currentDate = Date()
            scoresList.add(ScoreboardItem("Player $i", (i * 1000),currentDate))
        }

        // Set up recycler view
        recyclerView = findViewById(R.id.score_cycle)
        scoreboardAdapter = ScoreBoardAdapter(scoresList)
        recyclerView.adapter = scoreboardAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up back button
        val backButton = findViewById<Button>(R.id.scoreboard_back_button)
        backButton.setOnClickListener {
            finish() // Return to previous activity
        }
    }
}



