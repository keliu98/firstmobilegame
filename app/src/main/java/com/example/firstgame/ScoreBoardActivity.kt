package com.example.firstgame

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScoreBoardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreboardAdapter: ScoreBoardAdapter
    private lateinit var scoresList: MutableList<ScoreboardItem>
    private lateinit var database: ScoreDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        // Initialize the database
        database = ScoreDatabase(this)

        // Get the top scores from the database
        val topScores = database.getTopScores(10)

        // Set up recycler view
        recyclerView = findViewById(R.id.score_cycle)
        scoreboardAdapter = ScoreBoardAdapter(topScores)
        recyclerView.adapter = scoreboardAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up back button
        val backButton = findViewById<Button>(R.id.scoreboard_back_button)
        backButton.setOnClickListener {
            finish() // Return to previous activity
        }

        // Set up send email button
//        val sendEmailButton = findViewById<Button>(R.id.scoreboard_send_email_button)
//        sendEmailButton.setOnClickListener {
//            sendScoresByEmail(topScores)
//        }
    }

    private fun sendScoresByEmail(scores: List<ScoreboardItem>) {
        // TODO: Implement sending email
    }
}
