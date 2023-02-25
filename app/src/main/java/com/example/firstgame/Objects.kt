package com.example.firstgame

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*
import kotlin.math.max
import kotlin.random.Random

class RigidBody(
    xPos: Float = 0f,
    yPos: Float = 0f,
    xVel: Float = 0f,
    yVel: Float = 0f,
    xAcceleration: Float = 0f,
    yAcceleration: Float = 0f
) {

    var xPos: Float = xPos
    var yPos: Float = yPos
    var xVel: Float = xVel
    var yVel: Float = yVel
    var xAcceleration: Float = xAcceleration
    var yAcceleration: Float = yAcceleration

    fun Physics(deltaTime: Float, step: Int) {

        //Log.d("Step: ", step.toString())

        for (i in 0 until step) {
            xVel += xAcceleration * deltaTime
            yVel += yAcceleration * deltaTime
            //Log.d("xVel * deltaTime: ", (xVel * deltaTime).toString())
            xPos += xVel * deltaTime
            yPos += yVel * deltaTime
        }

    }

    fun SetPosition(x: Float, y: Float) {
        xPos = x
        yPos = y
    }

    fun SetVelocity(x: Float, y: Float) {
        xVel = x
        yVel = y
    }

}

class Rectangle(
    width: Float = 1f,
    height: Float = 1f,
    colour: Int = Color.LTGRAY,
) {
    var rectangle: RectF
    var paint: Paint

    var PosX: Float = 0f //center of sprite
    var PosY: Float = 0f //center of sprite
    var Width: Float
    var Height: Float

    init {
        Width = width
        Height = height

        rectangle = RectF()

        UpdateRectangle(PosX, PosY, Width, Height)

        paint = Paint().apply {
            color = colour
        }
    }

    /**
     * Call this whenever RigidBody has changed position
     */
    fun UpdateRectangle(x: Float, y: Float, width: Float, height: Float) {
        rectangle.top = y
        rectangle.bottom = y + height
        rectangle.left = x
        rectangle.right = x + width
    }

}

class AABBCollision(
    val left: Float = 0f,
    val top: Float = 0f,
    val right: Float = 0f,
    val bottom: Float = 0f
) {
    fun intersects(other: AABBCollision): Boolean {
        return right >= other.left && left <= other.right && bottom >= other.top && top <= other.bottom
    }
}

class Particle(
    var x: Float,
    var y: Float,
    val color: Int,
    val size: Float = 10f,
    val vx: Float = Random.nextFloat() * 200f,
    val vy: Float = Random.nextFloat() * 200f
)

class ParticleEntity(
    var xPos: Float,
    var yPos: Float,
    var Width: Float,
    var Height: Float,
    colour: Int
) {
    // new particle properties
    private val particles = ArrayList<Particle>()
    private val particleCount = 10
    private val particleInterval = 10 // in milliseconds

    private val particleHandler = android.os.Handler()

    private val maxParticleCount = 50 // maximum number of particles to display at a time

    private val particleRunnable = object : Runnable {
        override fun run() {
            addParticle()
            particleHandler.postDelayed(this, particleInterval.toLong())
        }
    }

    var paint: Paint

    init {
        particleHandler.postDelayed(particleRunnable, particleInterval.toLong())

        // create particles
        for (i in 0 until particleCount) {
            particles.add(Particle(xPos, yPos, Color.WHITE))
        }

        paint = Paint().apply {
            color = colour
        }
    }

    fun updateParticles(deltaTime: Float, step: Int) {
        for (i in 0 until step) {
            for (particle in particles) {
                particle.x += particle.vx * deltaTime
                particle.y += particle.vy * deltaTime
            }
        }
    }

    // add particle function
    private fun addParticle() {
        if (particles.size >= maxParticleCount) {
            particles.removeAt(0)
        }

        val particle = Particle(
            xPos + Utils.NegativeOneOrNot() * Random.nextFloat() * Width,
            yPos + Utils.NegativeOneOrNot() * Random.nextFloat() * Height,
            Color.WHITE,
            vx = Utils.NegativeOneOrNot() * Random.nextFloat() * 200f,
            vy = Utils.NegativeOneOrNot() * Random.nextFloat() * 200f
        )

        particles.add(particle)
    }

    // new render function
    fun render(canvas: Canvas) {

        // draw particles
        for (particle in particles) {
            paint.color = particle.color
            canvas.drawRect(
                particle.x - 0.5f * particle.size,
                particle.y - 0.5f * particle.size,
                particle.x + 0.5f * particle.size,
                particle.y + 0.5f * particle.size,
                paint
            )
        }
    }
}


open class GameObject(
    rb: RigidBody,
    sp: Rectangle,
    name: String = "GameObject",
    bitmap: Bitmap? = null,
) {
    var name: String = name
    var rigidBody: RigidBody = RigidBody()
    var rect: Rectangle = Rectangle()
    var collision: AABBCollision = AABBCollision()
    var bitmap: Bitmap? = bitmap

    // initializer block
    init {
        rigidBody = rb
        rect = sp

        if (bitmap != null) {
            rect.UpdateRectangle(
                rigidBody.xPos,
                rigidBody.yPos,
                bitmap.width.toFloat(),
                bitmap.height.toFloat()
            )
        }
        collision = AABBCollision(
            sp.rectangle.left,
            sp.rectangle.top,
            sp.rectangle.right,
            sp.rectangle.bottom
        )
    }

    /**
     * Override-able function that dictates initialization of any child GameObjects.
     */
    open fun Init() {
        /**
         * Don't put anything here! For inheritance only!
         */
    }

    /**
     * Basic updates required for GameObject. Not override-able! Override Behaviour() instead!
     */
    fun Update(deltaTime: Float, step: Int) {
        rigidBody.Physics(deltaTime, step)
        rect.UpdateRectangle(rigidBody.xPos, rigidBody.yPos, rect.Width, rect.Height)
        collision = AABBCollision(
            rect.rectangle.left,
            rect.rectangle.top,
            rect.rectangle.right,
            rect.rectangle.bottom
        )
        collision.intersects(other = AABBCollision())

        Log.d("GameObject: ", "${name}, PosX,Y: ${rigidBody.xPos},${rigidBody.yPos}")
    }

    /**
     * Override-able function that dictates Logic and Behaviour of any child GameObjects.
     * Needs root as there are multiple cases where we need to get the root during update,
     * especially stuff like onTouchListeners.
     */
    open fun Behaviour(root: ConstraintLayout) {

    }

    /**
     * By default draws a single bitmap, or if bitmap is not present, a rectangle with
     * default paint. Child Objects can override this to Draw differently, like say if they
     * want Animated Sprites instead (such as in Player).
     */
    open fun Draw(canvas: Canvas, paint: Paint) {
        if (bitmap == null) {
            Log.d("GameObject: ", "${name}, drawing rect: ${rect.rectangle.toString()}")
            canvas.drawRect(rect.rectangle, rect.paint)
        } else {

            canvas.drawBitmap(bitmap!!, null, rect.rectangle, null)
        }
    }
}

/**
 * Obstacle, inherits from GameObject
 */
class Obstacle(
    rb: RigidBody,
    sp: Rectangle,
    name: String = "GameObject",
    bitmap: Bitmap? = null,
    canvasWidth: Int,
) : GameObject(rb, sp, name, bitmap) {
    var obstacle_velocityX = -1200f
    var canvasW = canvasWidth
    var spacing = 1000
    var active = State.ACTIVE

    enum class State {
        ACTIVE,
        INACTIVE,
        EXIT
    }

    override fun Init() {
        this.rigidBody.xVel = obstacle_velocityX
    }

    override fun Behaviour(root: ConstraintLayout) {


        when (active) {
            State.ACTIVE -> {
                this.rigidBody.xVel = obstacle_velocityX

                if (this.rigidBody.xPos + this.rect.rectangle.width() < 0) {
//            var randomDeviation = Random.nextInt(0, 500)
//            this.rigidBody.xPos = canvasW + spacing + randomDeviation.toFloat()
                    active = State.INACTIVE
                }
            }

            State.INACTIVE -> {
                this.rigidBody.xVel = 0f
                this.rigidBody.xPos = -1000f //keep it in a safe place
            }

            State.EXIT -> {
                this.rigidBody.xVel = 0f
            }
        }

    }


}

/**
 * Player, inherits from GameObject.
 */
class Player(
    rb: RigidBody,
    sp: Rectangle,
    name: String = "GameObject",
    bitmap: Bitmap? = null,
    bitmap2: Bitmap? = null,
    ground: GameObject, //player needs to know where ground
    context: Context,
    thread: GameThread
) : GameObject(rb, sp, name, bitmap) {

    var mGround = ground
    var state = State.GROUND
    val gravity = 9000f
    val jumpVelocity = -3000f

    var frame = 0
    var maxFrame = 2
    var frames: List<Bitmap?> = listOf(bitmap, bitmap2)

    val animTimer = 0.2f
    var currentTime = 0f

    var context: Context = context
    var thread: GameThread = thread

    enum class State {
        JUMP,
        AIR,
        GROUND,
        KILLED,
        DEAD,
        END
    }

    init {
        Log.d("Ball: ", bitmap.toString())
    }

    override fun Init() {
        this.rigidBody.yPos = mGround.rect.rectangle.top - this.rect.rectangle.height()
    }

    override fun Behaviour(root: ConstraintLayout) {

        root.setOnTouchListener { view, event ->
            if (state != State.AIR && state != State.DEAD) {
                state = State.JUMP
            }
            true // return true to indicate that the touch event has been handled
        }

        when (state) {
            State.JUMP -> {
                this.rigidBody.yVel = jumpVelocity
                state = State.AIR
            }

            State.AIR -> {
                if (this.rect.rectangle.bottom < mGround.rect.rectangle.top) {
                    this.rigidBody.yAcceleration = gravity
                } else {
                    state = State.GROUND
                }
            }

            State.GROUND -> {
                this.rigidBody.yPos = mGround.rect.rectangle.top - this.rect.Height
                this.rigidBody.yAcceleration = 0f
                this.rigidBody.yVel = 0f

                if (currentTime > animTimer) {
                    frame += 1

                    if (frame >= maxFrame) {
                        frame = 0
                    }

                    currentTime = 0f
                } else {
                    currentTime += thread.Time.actualDeltaTime
                }

            }

            State.KILLED -> {
                this.rigidBody.yVel = jumpVelocity
                state = State.DEAD
            }

            State.DEAD -> {
                this.rigidBody.yAcceleration = 0.5f * gravity

                if (this.rigidBody.yPos >= 5000f) //Trigger to trigger end game menu
                {
                    (context as MainActivity).runOnUiThread()
                    {
                        //This is such bad code but it was the only way for player to access current score without
                        //having to take in a parameter in Behaviour()
                        (context as MainActivity).showMyDialog(root.findViewWithTag<GameView>("GameView").currentScore)
                    }
                    state = State.END
                }
            }
            State.END -> {
                //Do nothing
                this.rigidBody.yVel = 0f
                this.rigidBody.yAcceleration = 0f
                thread.setRunning(false)
            }
        }

        Log.d("Ball: ", state.toString())
    }

    override fun Draw(canvas: Canvas, paint: Paint) {
        if (frames[frame] == null) {
            Log.d("GameObject: ", "${name}, drawing rect: ${rect.rectangle.toString()}")
            canvas.drawRect(rect.rectangle, rect.paint)
        } else {
            canvas.drawBitmap(frames[frame]!!, null, rect.rectangle, null)
        }
    }

}


class Time {
    var elapsedTime = 0f
    var startTime = 0L
    var endTime = 0L
    var targetDeltaTime = 1 / 60f
    var actualDeltaTime = 0f
    var step = 1

    fun Update() {
        step = 0

        actualDeltaTime = (endTime - startTime) / 1000000000f
        elapsedTime += actualDeltaTime

        while (elapsedTime >= targetDeltaTime) {
            elapsedTime -= targetDeltaTime;
            step++;
        }
    }
}
