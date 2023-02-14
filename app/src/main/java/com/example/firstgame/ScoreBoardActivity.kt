package com.example.firstgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScoreBoardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreboardAdapter: ScoreBoardAdapter
    private lateinit var scoresList: MutableList<ScoreboardItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        // Initialize scores list
        scoresList = mutableListOf()
        for (i in 1..10) {
            scoresList.add(ScoreboardItem("Player $i", (i * 1000), "Feb 14, 2023"))
        }

        // Set up recycler view
        recyclerView = findViewById(R.id.score_cycle)
        scoreboardAdapter = ScoreBoardAdapter(scoresList)
        recyclerView.adapter = scoreboardAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}



