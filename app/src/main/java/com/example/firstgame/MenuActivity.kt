package com.example.firstgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val gameplay = findViewById<Button>(R.id.gameplaybutton)
        val scoreBoard = findViewById<Button>(R.id.ScoreBoardButton)
        val button3 = findViewById<Button>(R.id.button3)

        gameplay.setOnClickListener {
            // Add code for what should happen when button 1 is clicked

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        scoreBoard.setOnClickListener {
            // Add code for what should happen when button 2 is clicked
            val intent = Intent(this, ScoreBoardActivity::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {
            // Add code for what should happen when button 3 is clicked
        }
    }
}