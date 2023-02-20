package com.example.firstgame

import android.graphics.*

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

    fun UpdateRectangle(x: Float, y: Float, width: Float, height: Float) {
        rectangle.top = y + 0.5f * height
        rectangle.bottom = y - 0.5f * height
        rectangle.left = x - 0.5f * width
        rectangle.right = x + 0.5f * width
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

class GameObject(
    rb: RigidBody,
    sp: Rectangle,
    name: String = "GameObject"
) {
    var name: String = name
    var rigidBody: RigidBody = RigidBody()
    var rectangle: Rectangle = Rectangle()
    var collision: AABBCollision = AABBCollision()

    // initializer block
    init {
        rigidBody = rb
        rectangle = sp
        collision = AABBCollision(
            sp.rectangle.left,
            sp.rectangle.top,
            sp.rectangle.right,
            sp.rectangle.bottom
        )
    }

    fun Update(deltaTime: Float, step: Int) {
        rigidBody.Physics(deltaTime, step)
        rectangle.UpdateRectangle(rigidBody.xPos, rigidBody.yPos, rectangle.Width, rectangle.Height)
        collision = AABBCollision(
            rectangle.rectangle.left,
            rectangle.rectangle.top,
            rectangle.rectangle.right,
            rectangle.rectangle.bottom
        )
        collision.intersects(other = AABBCollision())
    }

}

class Time {
    var elapsedTime = 0f
    var startTime = 0L
    var endTime = 0L
    var deltaTime = 1 / 60f
    var actualDeltaTime = 0f
    var step = 1

    fun Update() {

        step = 0
        actualDeltaTime = (endTime - startTime) / 1000000000f
        elapsedTime += actualDeltaTime

        while (elapsedTime >= deltaTime) {
            elapsedTime -= deltaTime;
            step++;
        }
    }
}