package com.example.firstgame

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.abs
import kotlin.random.Random

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {


    /**
     * Game Engine stuff that keeps the game ticking
     */
    private val thread: GameThread //Make a Game Thread

    /**
     * Gameplay Managers
     */
    var noOfObstaclePool = 5
    var spawnTimer = 1f
    var elapsedTime = 0f

    var currentScore = 0

    var allGameObject: MutableList<GameObject> = mutableListOf()
    var allObstacle: MutableList<Obstacle> = mutableListOf()

    /**
     * Sprites to put into GameObject if needed
     */


    /**
     * Miscellaneous Gameplay Properties
     */

    private val paint = Paint().apply {
        color = Color.LTGRAY
    }


    /**
     * Game Objects
     */
    lateinit var ground: GameObject
    lateinit var ball: Player
    lateinit var particleEntity: ParticleEntity

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
            RigidBody(0f, height - 100f),
            Rectangle(width.toFloat(), 100f, Color.GREEN),
            "Ground",
            null
        )

        ball = Player(
            RigidBody(100f, 100f),
            Rectangle(236f, 200f, Color.GREEN),
            "Ball",
            BitmapFactory.decodeResource(resources, R.drawable.pickles1),
            BitmapFactory.decodeResource(resources, R.drawable.pickles2),
            ground,
            context,
            thread
        )

        particleEntity =
            ParticleEntity(
                150f,
                800f,
                100f,
                100f,
                Color.WHITE)

        /**
         * Make Obstacles
         */
        for (i in 0 until noOfObstaclePool) {
            allObstacle.add(
                Obstacle(
                    RigidBody(
                        /**width + i.toFloat() * 500**/
                        -1f, height - ground.rect.Height - 100f
                    ),
                    Rectangle(100f, 100f),
                    "Obstacle",
                    BitmapFactory.decodeResource(resources, R.drawable.golfball),
                    width
                )
            )
        }

        /**
         * Pass it to all Game Object List. This should be by reference, so no copies
         * are made. Hopefully.
         */
        for (i in 0 until noOfObstaclePool) {
            allGameObject.add(allObstacle[i])
        }

        allGameObject.add(ball)
        allGameObject.add(ground)

        for (i in 0 until allGameObject.size) {
            allGameObject[i].Init()
        }

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
         *
         * Recommend to also use (context as MainActivity).runOnUiThread() to update UI in real time!
         */

        (context as MainActivity).runOnUiThread(){
            val currentScoreText = (context as MainActivity).findViewById<TextView>(R.id.currentScore)
            currentScoreText.text = "Score: " + currentScore.toString()
        }

        val root =
            (context as MainActivity).findViewById<View>(R.id.main_layout) as ConstraintLayout


//        ball.Behaviour(root)
//        ball.Update(deltaTime, step)
//
//
//        ground.Update(deltaTime, step)

        for (i in 0 until allGameObject.size) {
            allGameObject[i].Behaviour(root)
            allGameObject[i].Update(deltaTime, step)
        }

        particleEntity.updateParticles(deltaTime, step)
        particleEntity.xPos = 1000f
        particleEntity.yPos = 700f
        particleEntity.Width = 1000f
        particleEntity.Height = 500f

        if (ball.state == Player.State.DEAD || ball.state == Player.State.END) {
            for (obstacle in allObstacle)
            {
                obstacle.active = Obstacle.State.EXIT
            }
        } else {
            for (i in 0 until allObstacle.size) {
                if (ball.collision.intersects(allObstacle[i].collision)) {
                    Log.d("Collide: ", "Dead")
                    ball.state = Player.State.KILLED
                }
            }

            /**
             * Obstacle Group Logic.
             */

            // Update score

            currentScore++

            if (elapsedTime > spawnTimer) {
                elapsedTime = 0f
                spawnTimer = Random.nextInt(1, 5).toFloat()

                for (obstacle in allObstacle) {
                    obstacle.obstacle_velocityX -= 100f
                }

                for (i in 0 until allObstacle.size) {
                    if (allObstacle[i].active == Obstacle.State.INACTIVE) {
                        allObstacle[i].active = Obstacle.State.ACTIVE
                        allObstacle[i].rigidBody.xPos = width.toFloat()
                        break
                    }
                }
            } else {
                elapsedTime += deltaTime * step
            }
        }


        //spawnTimer = Random.nextInt(1,1).toFloat()


    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)



        for (i in 0 until allGameObject.size) {
            allGameObject[i].Draw(canvas, paint)
        }

        particleEntity.render(canvas)



    }

}