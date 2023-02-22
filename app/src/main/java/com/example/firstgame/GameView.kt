package com.example.firstgame

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {

    private val thread: GameThread //Make a Game Thread

    private var currentScore = 0
    var running = true

    var Time: Time = Time()
    var noOfObstaclePool = 5
    var obstacles = mutableListOf<GameObject>()

    var bsprite: Rectangle = Rectangle(100f,100f, Color.GREEN)
    var brb: RigidBody = RigidBody()
    var ball = GameObject(brb,bsprite)

    private val paint = Paint().apply {
        color = Color.BLUE
    }

    //Add ur gameobjects here!
    private var obstacle: GameObject? = null

    init {
        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        //Init Game Object
        obstacle = GameObject(RigidBody(),Rectangle(100f,100f),"obstacle")
        obstacle!!.rigidBody.SetPosition(width/2f, height/2f)
        obstacle!!.rigidBody.SetVelocity(-800f,0f)

        // start the game thread
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true //While true, keep trying to kill the stupid thread if it somehow fails the first time

        while (retry) {
            try {
                /**
                 * Kill the Thread, otherwise you'll get an zombie thread!
                 */
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false //On success stop retrying
        }
    }

    fun pause() {
        // Stop the thread when the pause function is called
        running = false
        thread?.join()
    }

    fun resume() {
        // Create and start a new thread when the resume function is called
        running = true
//        thread = GameThread(holder)
        thread?.start()
    }

    /**
     * Function to update the positions of player and game objects
     */
//    fun GameLoop(deltaTime: Float, step: Int, canvas: Canvas)
    fun GameLoop(canvas: Canvas) {

//        if(obstacle!!.rigidBody.xPos < 10){
//            obstacle!!.rigidBody.SetVelocity(800f,0f)
//        }
//
//        if(obstacle!!.rigidBody.xPos > width - 10 ){
//            obstacle!!.rigidBody.SetVelocity(-800f,0f)
//        }
//
//        obstacle!!.Update(deltaTime,step)

        val ground = (context as MainActivity).findViewById<View>(R.id.ground) as LinearLayout
        val currentScoreText = (context as MainActivity).findViewById<TextView>(R.id.currentScore)
        val root = (context as MainActivity).findViewById<View>(R.id.main_layout) as ConstraintLayout
        currentScoreText.text = "Score: " + currentScore.toString()

        ball.rigidBody.xPos = 100f
        // Reset ball velocity when it hits the ground
        if (ball.rigidBody.yPos + ball.rectangle.Height/2 >= ground.top) {
            ball.rigidBody.yVel = 0f
        }

        root.setOnTouchListener { view, event ->
            if (ball.rigidBody.yPos + ball.rectangle.Height >= ground.top) {
                ball.rigidBody.yVel = -750f
                ball.rigidBody.yPos = ball.rigidBody.yPos - 10
            }

            Log.d("Listener", ball.rigidBody.yVel.toString())
            Log.d("Listener:X", ball.rigidBody.xPos.toString())
            Log.d("Listener:Y", ball.rigidBody.yPos.toString())
            true // return true to indicate that the touch event has been handled
        }

        // Update ball physics
        val gravity = 800f // The strength of gravity, in pixels per second squared

        // Update ball's y acceleration to include gravity
        if (ball.rigidBody.yPos + ball.rectangle.Height < ground.top) {
            ball.rigidBody.yAcceleration = gravity
            Log.d("AIR", "AIR")
            Log.d("AIR:X", ball.rigidBody.xPos.toString())
            Log.d("AIR:Y", ball.rigidBody.yPos.toString())
        } else {
            ball.rigidBody.yAcceleration = 0f
            Log.d("GROUND", "GROUND")
            Log.d("GROUND:X", ball.rigidBody.xPos.toString())
            Log.d("GROUND:Y", ball.rigidBody.yPos.toString())
        }

        //Spawner
        for(i in 0 until obstacles.size)
        {
            if(obstacles[i].rigidBody.xPos < 0)
            {
                obstacles[i].rigidBody.xPos = Random.nextInt(2000,9999).toFloat()
            }
        }

        for(i in 0 until obstacles.size)
        {
            obstacles[i].Update(Time.targetDeltaTime, 1)
            canvas.drawRect(obstacles[i].rectangle.rectangle, obstacles[i].rectangle.paint)
        }

        // Collision detection
        val ballRect = Rect(
            (ball.rigidBody.xPos - ball.rectangle.Width/2).toInt(),
            (ball.rigidBody.yPos - ball.rectangle.Height/2).toInt(),
            (ball.rigidBody.xPos + ball.rectangle.Width/2).toInt(),
            (ball.rigidBody.yPos + ball.rectangle.Height/2).toInt()
        )

        for (obstacle in obstacles) {
            val obstacleRect = Rect(
                (obstacle.rigidBody.xPos - obstacle.rectangle.Width/2).toInt(),
                (obstacle.rigidBody.yPos - obstacle.rectangle.Height/2).toInt(),
                (obstacle.rigidBody.xPos + obstacle.rectangle.Width/2).toInt(),
                (obstacle.rigidBody.yPos + obstacle.rectangle.Height/2).toInt()
            )
            if (ballRect.intersect(obstacleRect)) {
                // Collision detected
                Log.d("GAMELOOP", "END")
                val intent = Intent(context, GameOverActivity::class.java)
                intent.putExtra("score", currentScore) // Pass score to GameOverActivity
                context.startActivity(intent) // Open GameOverActivity
                running=false
                return // Stop the game loop
            }
        }

        // Update obstacles and draw them
        for (obstacle in obstacles) {
            obstacle.Update(Time.targetDeltaTime, 1)
            canvas.drawRect(obstacle.rectangle.rectangle, obstacle.rectangle.paint)
        }
        ball.Update(Time.targetDeltaTime, 1)
        canvas.drawRect(ball.rectangle.rectangle, ball.rectangle.paint)

        // Update score
        currentScore++

    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if(running)
        {
            GameLoop(canvas)
        }
        else
        {
            return
        }
//        canvas.drawRect(obstacle!!.rectangle.rectangle, paint)
    }

}