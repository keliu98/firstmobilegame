package com.example.firstgame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class GameOverActivity: AppCompatActivity() {


    private lateinit var scoreView : ScoreBoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)
        val score = intent.getIntExtra("score", 0)
        scoreView = ViewModelProvider(this)[ScoreBoardViewModel::class.java] //Get the Viewmodel

        CoroutineScope(Dispatchers.IO).launch {
            val finalScore = ScoreboardItem(name = "Player" ,score = score, date = Utils.FormatDate(
                Date()
            ))
            scoreView.insert(finalScore)
        }

        val textScore = findViewById<TextView>(R.id.text_score)

        textScore.text = "Score: $score"

        val shareBtn = findViewById<Button>(R.id.share_score)
        shareBtn.setOnClickListener {
            // Do something when button 1 is clicked
            val intent = Intent(Intent.ACTION_SENDTO).apply {

                data = Uri.parse("mailto:")
                putExtra(
                    Intent.EXTRA_EMAIL, arrayOf("recipient@example.com")
                ) // Set the email address of the recipient
                putExtra(Intent.EXTRA_SUBJECT, "My latest score") // Set the subject of the email
                putExtra(Intent.EXTRA_TEXT, textScore.text) // Set the body of the email
            }
            startActivity(intent)
        }

        val menuBtn = findViewById<Button>(R.id.exitgame_button)
        menuBtn.setOnClickListener {
            // Do something when button 2 is clicked
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

            //dialog.dismiss() // Close the dialog when done
        }
    }
}