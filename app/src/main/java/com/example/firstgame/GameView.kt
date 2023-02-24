package com.example.firstgame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

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

    private var currentScore = 0

    /**
     * Sprites to put into GameObject if needed
     */

    //TODO Change to ImageView
    //var groundImageView:LinearLayout = (context as MainActivity).findViewById<View>(R.id.ground) as LinearLayout



    /**
     * Add all the other views that you would like to update and run during gameplay
     * moments!
     */
    val currentScoreText = (context as MainActivity).findViewById<TextView>(R.id.currentScore)
    val root = (context as MainActivity).findViewById<View>(R.id.main_layout) as ConstraintLayout

    /**
     * Game Objects
     */

    var ground = GameObject(
        RigidBody(0f,2000f),
        Rectangle(width.toFloat(), 100f),
        "Ground"
    )

    var bsprite: Rectangle = Rectangle(100f, 100f, Color.GREEN)
    var brb: RigidBody = RigidBody()

    var ball: Player

    /**
     * Miscellaneous Gameplay Properties
     */

    //val gravity = 800f // The strength of gravity, in pixels per second squared

    private val paint = Paint().apply {
        color = Color.LTGRAY
    }



    init {
        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)

        var ballImageView: ImageView = (context as MainActivity).findViewById<View>(R.id.ball) as ImageView
        ball = Player(
            brb,
            bsprite,
            "Ball",
            ballImageView,
            root,
            ground)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        //Init Game Objects


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
        currentScoreText.text = "Score: " + currentScore.toString()


//        /**
//         * User Input Logic
//         */
//        root.setOnTouchListener { view, event ->
//            if (ball.rigidBody.yPos + ball.rect.Height >= ground.rect.rectangle.top) {
//                ball.rigidBody.yVel = -750f
//                ball.rigidBody.yPos = ball.rigidBody.yPos - 10
//            }
//
//            Log.d("Listener", ball.rigidBody.yVel.toString())
//            Log.d("Listener:X", ball.rigidBody.xPos.toString())
//            Log.d("Listener:Y", ball.rigidBody.yPos.toString())
//            true // return true to indicate that the touch event has been handled
//        }
        ball.Behaviour()
        ball.Update(deltaTime, step)





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