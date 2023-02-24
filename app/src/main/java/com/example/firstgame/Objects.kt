package com.example.firstgame

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*
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

/**
 * Note if image is provided, parameters width and height and color are ignored!
 */
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
        rectangle.bottom = y - height
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
        return right >= other.left && left <= other.right && bottom <= other.top && top >= other.bottom
    }
}

open class GameObject(
    rb: RigidBody,
    sp: Rectangle,
    name: String = "GameObject",
    imageView: ImageView? = null,
) {
    var name: String = name
    var rigidBody: RigidBody = RigidBody()
    var rect: Rectangle = Rectangle()
    var collision: AABBCollision = AABBCollision()
    var bitmap: Bitmap? = (imageView!!.drawable as BitmapDrawable).bitmap

    // initializer block
    init {
        rigidBody = rb
        rect = sp

        if (bitmap != null) {
            rect.UpdateRectangle(
                imageView!!.x.toFloat(),
                imageView.y.toFloat(),
                imageView.width.toFloat(),
                imageView.height.toFloat()
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
    }

    /**
     * Override-able function that dictates Logic and Behaviour of any child GameObjects.
     */
    open fun Behaviour() {
        /**
         * Don't put anything here! For inheritance only!
         */
    }

    fun Draw(canvas: Canvas, paint: Paint) {
        if (bitmap == null) {
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
    canvasWidth: Int,
) : GameObject(rb, sp, name) {
    var obstacle_velocityX = -500f
    var canvasW = canvasWidth

    override fun Init() {
        this.rigidBody.xVel = obstacle_velocityX
    }

    override fun Behaviour() {
        if (this.rigidBody.xPos < 0) {
            var randomPosX = Random.nextInt(canvasW, canvasW + 1000)
            this.rigidBody.xPos = randomPosX.toFloat()
        }
    }
}

class Player(
    rb: RigidBody,
    sp: Rectangle,
    name: String = "GameObject",
    imageView: ImageView? = null,
    root: ConstraintLayout, //For player to access the whole main view
    ground: GameObject
) : GameObject(rb, sp, name, imageView) {

    val mRoot = root
    var mGround = ground
    var state = State.GROUND
    val gravity = 981f
    val jumpVelocity = -800f

    enum class State {
        JUMP,
        AIR,
        GROUND
    }

    override fun Init() {
        this.rigidBody.xPos = mGround.rect.rectangle.top + this.rect.rectangle.height()
    }

    override fun Behaviour() {
        mRoot.setOnTouchListener { view, event ->
            state = State.JUMP
            true // return true to indicate that the touch event has been handled
        }

        when (state) {
            State.JUMP -> {
                this.rigidBody.yVel = jumpVelocity
                state = State.AIR
            }

            State.AIR -> {
                if (this.rect.rectangle.bottom > mGround.rect.rectangle.top) {
                    this.rigidBody.yAcceleration = gravity
                } else {
                    state = State.GROUND
                }
            }

            State.GROUND -> {
                this.rigidBody.xPos = mGround.rect.rectangle.top
                this.rigidBody.yAcceleration = 0f
                this.rigidBody.yVel = 0f
            }
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