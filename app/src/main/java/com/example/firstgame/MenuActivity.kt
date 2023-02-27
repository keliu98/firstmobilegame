package com.example.firstgame

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val gameplay = findViewById<Button>(R.id.gameplaybutton)
        val scoreBoard = findViewById<Button>(R.id.ScoreBoardButton)
        ViewModelProvider(this)[ScoreBoardViewModel::class.java] //Get the Viewmodel

        gameplay.setOnClickListener {
            // Add code for what should happen when play button is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        scoreBoard.setOnClickListener {
            // Add code for what should happen when scoreboard button is clicked
            val intent = Intent(this, ScoreBoardActivity::class.java)
            startActivity(intent)
        }
    }
}