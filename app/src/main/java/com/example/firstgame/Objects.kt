package com.example.firstgame

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.widget.ImageView

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

        Log.d("Step: ", step.toString())

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

class Sprite(
    width: Float = 1f,
    height: Float = 1f,
    colour: Int = Color.MAGENTA
) {
    var rectangle: RectF
    var paint: Paint

    var PosX: Float = 0f
    var PosY: Float = 0f
    var Width: Float
    var Height: Float

    init {
        rectangle = RectF()
        Width = width
        Height = height

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
//
//        rectangle.top = 300f
//        rectangle.bottom =  -300f
//        rectangle.left = -300f
//        rectangle.right = 300f
    }

}

class GameObject(
    rb: RigidBody,
    sp: Sprite
) {
    var rigidBody: RigidBody = RigidBody()
    var sprite: Sprite = Sprite()

    // initializer block
    init {
        rigidBody = rb
        sprite = sp
    }

    fun Update(deltaTime: Float, step: Int) {
        rigidBody.Physics(deltaTime, step)
        sprite.UpdateRectangle(rigidBody.xPos, rigidBody.yPos, sprite.Width, sprite.Height)
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