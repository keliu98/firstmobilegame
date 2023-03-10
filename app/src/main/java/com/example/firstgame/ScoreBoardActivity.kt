package com.example.firstgame

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ScoreBoardActivity : AppCompatActivity(), java.io.Serializable {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreboardAdapter: ScoreBoardAdapter
    lateinit var scoresList: MutableList<ScoreboardItem>
    private val viewModel: ScoreBoardViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Initialize scores list
        scoresList = mutableListOf()

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

        // Set up back button
        val resetButton = findViewById<Button>(R.id.scoreboard_reset_button)
        resetButton.setOnClickListener {
            //ALWAYS do a Coroutine Scope, Room crashes otherwise!
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteAllScores()
            }
        }

        //Database ALWAYS returns LiveData, and hence an observer is always needed to read LiveData.
        viewModel.allData.observe(this) {
            scoreboardAdapter.setScoreBoardWithIndex(it)
        }
    }

    /**
     * Helper function to convert Data to String.
     */
    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}



