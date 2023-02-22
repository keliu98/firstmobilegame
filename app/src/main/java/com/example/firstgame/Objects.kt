package com.example.firstgame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.widget.ImageView
import java.util.logging.Handler
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

class Particle(
    var x: Float,
    var y: Float,
    val color: Int,
    val size: Float = 10f,
    val vx: Float = Random.nextFloat() * 2f ,
    val vy: Float = Random.nextFloat() * 2f
)


class Sprite(
    width: Float = 1f,
    height: Float = 1f,
    colour: Int = Color.RED
) {
    var rectangle: RectF
    var paint: Paint

    var PosX: Float = 0f
    var PosY: Float = 0f
    var Width: Float
    var Height: Float

    // new particle properties
    private val particles = ArrayList<Particle>()
    private val particleCount = 10
    private val particleInterval = 100 // in milliseconds
    private val particleHandler = android.os.Handler()
    private val particleRunnable = object : Runnable {
        override fun run() {
            addParticle()
            particleHandler.postDelayed(this, particleInterval.toLong())
        }
    }
    init {
        rectangle = RectF()
        Width = width
        Height = height

        UpdateRectangle(PosX, PosY, Width, Height)
        particleHandler.postDelayed(particleRunnable, particleInterval.toLong())

        // create particles
        for (i in 0 until particleCount) {
            particles.add(Particle(PosX, PosY, Color.WHITE))
        }

        paint = Paint().apply {
            color = colour
        }
    }

    fun updateParticles() {
        for (particle in particles) {
            particle.x += particle.vx
            particle.y += particle.vy
        }
    }


    private val maxParticleCount = 50 // maximum number of particles to display at a time

    // add particle function
    private fun addParticle() {
        if (particles.size >= maxParticleCount) {
            particles.removeAt(0)
        }
        val particle = Particle(
            PosX + Random.nextFloat() * Width,
            PosY + Random.nextFloat() * Height,
            Color.WHITE
        )
        particles.add(particle)
    }


    // existing UpdateRectangle function

    // new render function
    fun render(canvas: Canvas) {

        // update particles
        updateParticles()

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
    sp: Sprite,
    //part: Particle
) {
    var rigidBody: RigidBody = RigidBody()
    var sprite: Sprite = Sprite()
    //var particle: Particle = Particle(1f,1f,Color.RED )

    // initializer block
    init {
        rigidBody = rb
        sprite = sp
       // particle = part
    }

    fun Update(deltaTime: Float, step: Int) {
        rigidBody.Physics(deltaTime, step)
        sprite.UpdateRectangle(rigidBody.xPos, rigidBody.yPos, sprite.Width, sprite.Height)
        //particle.
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