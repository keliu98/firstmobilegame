package com.example.firstgame

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

//class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs){
//
//    private var currentScore = 0
//    var running = true
//
//    var Time: Time = Time()
//    var noOfObstaclePool = 5
//    var obstacles = mutableListOf<GameObject>()
//
//    var bsprite: Rectangle = Rectangle(100f,100f, Color.GREEN)
//    var brb: RigidBody = RigidBody()
//    var ball = GameObject(brb,bsprite)
//
//    init{
//        for (i in 0 until noOfObstaclePool)
//        {
//            var rectangle: Rectangle = Rectangle(100f,100f)
//
//            //setting xPos to -1 forces spawner to respawn object at random places on start
//            var rb: RigidBody = RigidBody(-1f,850f,-550f)
//
//            var obstacle = GameObject(rb,rectangle)
//            obstacles.add(obstacle)
//        }
//    }
//
//    private val paint = Paint().apply {
//        color = Color.BLUE
//    }
//
//    private var obstaclePosition = 0f
//    private val obstacleWidth = 100f
//
//    private val animator = ValueAnimator.ofFloat(1f, 0f).apply {
//        interpolator = LinearInterpolator()
//        duration = 3000 // 1 second = 1000
//        repeatCount = ValueAnimator.INFINITE
//        repeatMode = ValueAnimator.RESTART
//        addUpdateListener { animator ->
//            obstaclePosition = animator.animatedValue as Float
//            invalidate()
//        }
//    }
//
//    private fun GameLoop(canvas: Canvas)
//    {
//        val ground = (context as MainActivity).findViewById<View>(R.id.ground) as LinearLayout
//        val currentScoreText = (context as MainActivity).findViewById<TextView>(R.id.currentScore)
//
//        currentScoreText.text = "Score: " + currentScore.toString()
//
//        ball.rigidBody.xPos = 100f
//        // Reset ball velocity when it hits the ground
//        if (ball.rigidBody.yPos + ball.rect.Height/2 >= ground.top) {
//            ball.rigidBody.yVel = 0f
//        }
//
//        root.setOnTouchListener { view, event ->
//            if (ball.rigidBody.yPos + ball.rect.Height >= ground.top) {
//                ball.rigidBody.yVel = -750f
//                ball.rigidBody.yPos = ball.rigidBody.yPos - 10
//            }
//
//            Log.d("Listener", ball.rigidBody.yVel.toString())
//            Log.d("Listener:X", ball.rigidBody.xPos.toString())
//            Log.d("Listener:Y", ball.rigidBody.yPos.toString())
//            true // return true to indicate that the touch event has been handled
//        }
//
//        // Update ball physics
//        val gravity = 800f // The strength of gravity, in pixels per second squared
//
//        // Update ball's y acceleration to include gravity
//        if (ball.rigidBody.yPos + ball.rect.Height < ground.top) {
//            ball.rigidBody.yAcceleration = gravity
//            Log.d("AIR", "AIR")
//            Log.d("AIR:X", ball.rigidBody.xPos.toString())
//            Log.d("AIR:Y", ball.rigidBody.yPos.toString())
//        } else {
//            ball.rigidBody.yAcceleration = 0f
//            Log.d("GROUND", "GROUND")
//            Log.d("GROUND:X", ball.rigidBody.xPos.toString())
//            Log.d("GROUND:Y", ball.rigidBody.yPos.toString())
//        }
//
//        //Spawner
//        for(i in 0 until obstacles.size)
//        {
//            if(obstacles[i].rigidBody.xPos < 0)
//            {
//                obstacles[i].rigidBody.xPos = Random.nextInt(2000,9999).toFloat()
//            }
//        }
//
//        for(i in 0 until obstacles.size)
//        {
//            obstacles[i].Update(Time.targetDeltaTime, 1)
//            canvas.drawRect(obstacles[i].rect.rectangle, obstacles[i].rect.paint)
//        }
//
//        // Collision detection
//        val ballRect = Rect(
//            (ball.rigidBody.xPos - ball.rect.Width/2).toInt(),
//            (ball.rigidBody.yPos - ball.rect.Height/2).toInt(),
//            (ball.rigidBody.xPos + ball.rect.Width/2).toInt(),
//            (ball.rigidBody.yPos + ball.rect.Height/2).toInt()
//        )
//
//        for (obstacle in obstacles) {
//            val obstacleRect = Rect(
//                (obstacle.rigidBody.xPos - obstacle.rect.Width/2).toInt(),
//                (obstacle.rigidBody.yPos - obstacle.rect.Height/2).toInt(),
//                (obstacle.rigidBody.xPos + obstacle.rect.Width/2).toInt(),
//                (obstacle.rigidBody.yPos + obstacle.rect.Height/2).toInt()
//            )
//            if (ballRect.intersect(obstacleRect)) {
//                // Collision detected
//                Log.d("GAMELOOP", "END")
//                val intent = Intent(context, GameOverActivity::class.java)
//                intent.putExtra("score", currentScore) // Pass score to GameOverActivity
//                context.startActivity(intent) // Open GameOverActivity
//                running=false
//                return // Stop the game loop
//            }
//        }
//
//        // Update obstacles and draw them
//        for (obstacle in obstacles) {
//            obstacle.Update(Time.targetDeltaTime, 1)
//            canvas.drawRect(obstacle.rect.rectangle, obstacle.rect.paint)
//        }
//        ball.Update(Time.targetDeltaTime, 1)
//        canvas.drawRect(ball.rect.rectangle, ball.rect.paint)
//
//        // Update score
//        currentScore++
//    }
//
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        animator.start()
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        animator.cancel()
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        if(running)
//        {
//            GameLoop(canvas)
//        }
//        else
//        {
//            return
//        }
//    }
//}

