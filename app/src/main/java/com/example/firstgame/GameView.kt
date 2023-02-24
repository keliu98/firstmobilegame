package com.example.firstgame

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {

    /**
     * Game Engine stuff that keeps the game ticking
     */
    private val thread: GameThread //Make a Game Thread
    var Time: Time = Time()


    /**
     * Gameplay Managers
     */
    var noOfObstaclePool = 5

    var currentScore = 0

    /**
     * Sprites to put into GameObject if needed
     */

    //TODO Change to ImageView
    //var groundImageView:LinearLayout = (context as MainActivity).findViewById<View>(R.id.ground) as LinearLayout


    /**
     * Miscellaneous Gameplay Properties
     */

    //val gravity = 800f // The strength of gravity, in pixels per second squared

    private val paint = Paint().apply {
        color = Color.LTGRAY
    }



    /**
     * Game Objects
     */

    lateinit var ground : GameObject

    lateinit var ball : Player



    init {
        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)

        Log.d("Canvas: ", "Width: ${width.toString()}, Height: ${height.toString()}")


    }

    /**
     * This is when the actual view is created. Recommend to init game objects here, especially if
     * they depend on view properties such as Canvas width and height, as they are only guaranteed
     * to be updated before surface created!
     */
    override fun surfaceCreated(holder: SurfaceHolder) {
        //Init Game Objects
        Log.d("Canvas: ", "Width: ${width.toString()}, Height: ${height.toString()}")

        ground = GameObject(
            RigidBody(0f,height - 100f),
            Rectangle(width.toFloat(), 100f, Color.GREEN),
            "Ground",
            null
        )

        ball = Player(
            RigidBody(100f,100f),
            Rectangle(236f, 200f, Color.GREEN),
            "Ball",
            BitmapFactory.decodeResource(resources, R.drawable.pngegg),
            ground)

        // start the game thread
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry =
            true //While true, keep trying to kill the thread if it somehow fails the first time

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

    /**
     * Function to update the positions of player and game objects
     */
    fun GameLoop(deltaTime: Float, step: Int, canvas: Canvas) {

        /**
         * Add all the other views that you would like to update and run during gameplay
         * moments! Have to be done in update loop as other views may not be instantiated when
         * GameView was created!
         */
        val currentScoreText = (context as MainActivity).findViewById<TextView>(R.id.currentScore)
        currentScoreText.text = "Score: " + currentScore.toString()

        val root = (context as MainActivity).findViewById<View>(R.id.main_layout) as ConstraintLayout

        ball.Behaviour(root)
        ball.Update(deltaTime, step)

        
        ground.Update(deltaTime, step)



        // Update score
        currentScore++

    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        ball.Draw(canvas, paint)
        ground.Draw(canvas, paint)

    }

}