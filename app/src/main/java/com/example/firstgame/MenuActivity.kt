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
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)

        gameplay.setOnClickListener {
            // Add code for what should happen when button 1 is clicked

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            // Add code for what should happen when button 2 is clicked
        }

        button3.setOnClickListener {
            // Add code for what should happen when button 3 is clicked
        }
    }
}