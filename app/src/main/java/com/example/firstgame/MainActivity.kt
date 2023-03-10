package com.example.firstgame

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var xPos = 0f
    private var xAcceleration = 0f
    private var xVelocity = 0.0f
    private var yPos = 0f
    private var yVelocity = 0.0f
    private var screenWidth = 0f
    private var rotation = 0f
    private var ball: ImageView? = null
    private var ground: LinearLayout? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        runOnUiThread{

        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels.toFloat()
    }

     fun showMyDialog(currentScore: Int) {
        var scoreView : ScoreBoardViewModel = ViewModelProvider(this)[ScoreBoardViewModel::class.java] //Get the Viewmodel
        val dialog = Dialog(this)

        //dialog.setCancelable(false) <- This wont work

        dialog.setContentView(R.layout.popup_layout)
        dialog.setCancelable(false) //must be here

        // Set up click listeners for any buttons in the pop-up window
        val send_email_button = dialog.findViewById<Button>(R.id.share_score)
        val back_to_menu_button = dialog.findViewById<Button>(R.id.exitgame_button)
        val text_score = dialog.findViewById<TextView>(R.id.text_score)

        //Database ALWAYS returns LiveData, and hence an observer is always needed to read LiveData.
        scoreView.lastScoreItem.observe(this) {
            var scoreToDisplay = ""
            if(currentScore == 0){
                //Game has not started yet, display last score
                scoreToDisplay = scoreView.lastScoreItem.value?.score.toString()
                if(scoreToDisplay == "null")
                {
                    //if last score is null, also put 0
                    scoreToDisplay = 0.toString()
                }
            }
            else{
                scoreToDisplay = currentScore.toString()
            }
            text_score.text = "Score: " + scoreToDisplay
        }

        send_email_button.setOnClickListener {
            // Do something when button 1 is clicked
            val intent = Intent(Intent.ACTION_SENDTO).apply {

                data = Uri.parse("mailto:")
                putExtra(
                    Intent.EXTRA_EMAIL, arrayOf("recipient@example.com")
                ) // Set the email address of the recipient
                putExtra(Intent.EXTRA_SUBJECT, "My latest score") // Set the subject of the email
                putExtra(Intent.EXTRA_TEXT, text_score.text) // Set the body of the email
            }
            startActivity(intent)
        }

         /**
          * Back to main menu
          */
         back_to_menu_button.setOnClickListener {
            // Do something when button 2 is clicked
            CoroutineScope(Dispatchers.IO).launch {
                val finalScore = ScoreboardItem(name = "Player" ,score = currentScore, date = Utils.FormatDate(Date()))
                scoreView.insert(finalScore)
            }

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        updateXAccel(sensorEvent)
        updateX()
        updateY()
        ball!!.y = yPos
        rotation += xVelocity / 2.5f
        ball!!.rotation = rotation
    }

    fun updateXAccel(sensorEvent: SensorEvent) {
        xAcceleration = -sensorEvent.values[1]
    }

    fun updateX() {
        xVelocity -= xAcceleration * 0.3f
        if (ballRightSideWithSpeed() > screenWidth) {
            xPos = screenWidth - ball!!.width
            xVelocity = 0f
        } else if (ballLeftSideWithSpeed() < 0) {
            xVelocity = 0f
            xPos = xVelocity
        } else {
            xPos += xVelocity
        }
    }

    fun updateY() {
        if (ballIsHittingTheGround()) {
            yPos = ground!!.y - ball!!.height
            yVelocity = 0f
        }
        else {
            yPos += yVelocity
            yVelocity += 1.5f //gravity
        }
    }


    private fun ballRightSide(): Float {
        return xPos + ball!!.width
    }

    private fun ballBottomSide(): Float {
        return ball!!.y + ball!!.height
    }

    private fun ballRightSideWithSpeed(): Float {
        return ballRightSide() + xVelocity
    }

    private fun ballLeftSideWithSpeed(): Float {
        return xPos + xVelocity
    }

    private fun ballBottomSideWithSpeed(): Float {
        return ballBottomSide() + yVelocity
    }

    private fun ballIsOnTheGround(): Boolean {
        return ballBottomSide() == ground!!.y
    }

    private fun ballIsHittingTheGround(): Boolean {
        return ballBottomSideWithSpeed() > ground!!.y
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
}