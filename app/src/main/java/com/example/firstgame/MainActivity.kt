package com.example.firstgame

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.graphics.Camera
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var xPos = 0f
    private var xAcceleration = 0f
    private var xVelocity = 0.0f
    private var yPos = 0f
    private var yVelocity = 0.0f
    private var screenWidth = 0f
    private var rotation = 0f
    private var sensorManager: SensorManager? = null
    private var ball: ImageView? = null
    private var ground: LinearLayout? = null
    private var obstacle: LinearLayout? = null
    private var obstacle1: LinearLayout? = null

    private lateinit var scoreView : ScoreBoardViewModel

    //private var scoreList = LiveData<List<ScoreboardItem>>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        scoreView = ViewModelProvider(this)[ScoreBoardViewModel::class.java] //Get the Viewmodel

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        ball = findViewById<View>(R.id.ball) as ImageView
        ground = findViewById<View>(R.id.ground) as LinearLayout
        obstacle = findViewById<View>(R.id.obstacle) as LinearLayout

        val root = findViewById<View>(R.id.main_layout) as ConstraintLayout
        root.setOnTouchListener { view, event ->
            if (ballIsOnTheGround() || ballIsOnTheObstacle()) {
                yVelocity = -40f
            }
            true
        }
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels.toFloat()


        val button_test = findViewById<Button>(R.id.test_email_button)
        button_test.setOnClickListener {
            // Add code for what should happen when button 1 is clicked
            showDialog(button_test)
        }
    }

    private fun showDialog(viewWhenClicked: View) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_layout)



        //get the last element of the score list and the score of that last element

        //Log.d("Last Score Item: ", lastScoreItem.value?.score.toString())

        // Set up click listeners for any buttons in the pop-up window
        val button1 = dialog.findViewById<Button>(R.id.share_score)
        val button2 = dialog.findViewById<Button>(R.id.cancel_button)
        val text_score = dialog.findViewById<TextView>(R.id.text_score)

        scoreView.lastScoreItem.observe(this) {
            text_score.text = "Score: " + scoreView.lastScoreItem.value?.score.toString()
        }

        button1.setOnClickListener {
            // Do something when button 1 is clicked
            val intent = Intent(Intent.ACTION_SENDTO).apply {

                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("recipient@example.com")) // Set the email address of the recipient
                putExtra(Intent.EXTRA_SUBJECT, "My latest score") // Set the subject of the email
                putExtra(Intent.EXTRA_TEXT, text_score.text) // Set the body of the email
            }
            startActivity(intent)
        }

        button2.setOnClickListener {
            // Do something when button 2 is clicked
            dialog.dismiss() // Close the dialog when done
        }

        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        sensorManager!!.registerListener(
            this, sensorManager!!.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ), SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onStop() {
        sensorManager!!.unregisterListener(this)
        super.onStop()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        updateXAccel(sensorEvent)
        updateX()
        updateY()
        ball!!.x = xPos
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
        } else if (ballIsHittingTheObstacleFromLeft() && ballBottomSide() > obstacleTopSide()) {
            xPos = obstacleLeftSide() - ball!!.width
            xVelocity = 0f
        } else if (ballIsHittingTheObstacleFromRight() && ballBottomSide() > obstacleTopSide()) {
            xPos = obstacleRightSide()
            xVelocity = 0f
        } else {
            xPos += xVelocity
        }
    }

    fun updateY() {
        if (ballIsHittingTheGround()) {
            yPos = ground!!.y - ball!!.height
            yVelocity = 0f
        } else if (ballIsHittingTheObstacleFromTop() && ballRightSideWithSpeed() > obstacleLeftSide() && ballLeftSideWithSpeed() < obstacleRightSide()) {
            yPos = obstacleTopSide() - ball!!.height
            yVelocity = 0f
        } else {
            yPos += yVelocity
            yVelocity += 2f
        }
    }

    private fun obstacleTopSide(): Float {
        return obstacle!!.y
    }

    private fun obstacleRightSide(): Float {
        return obstacle!!.x + obstacle!!.width
    }

    private fun obstacleLeftSide(): Float {
        return obstacle!!.x
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

    private fun ballIsOnTheObstacle(): Boolean {
        return ballIsHittingTheObstacleFromTop() &&
                (ballIsHittingTheObstacleFromLeft() || ballIsHittingTheObstacleFromRight())
    }

    private fun ballIsHittingTheGround(): Boolean {
        return ballBottomSideWithSpeed() > ground!!.y
    }

    private fun ballIsHittingTheObstacleFromTop(): Boolean {
        return ballBottomSide() <= obstacleTopSide() &&
                ballBottomSideWithSpeed() >= obstacleTopSide()
    }

    private fun ballIsHittingTheObstacleFromLeft(): Boolean {
        return ballRightSideWithSpeed() > obstacleLeftSide() &&
                ballLeftSideWithSpeed() < obstacleLeftSide()
    }

    private fun ballIsHittingTheObstacleFromRight(): Boolean {
        return ballLeftSideWithSpeed() < obstacleRightSide() &&
                ballRightSideWithSpeed() > obstacleRightSide()
    }



    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
}