package com.example.firstgame

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import java.util.*
import kotlin.random.Random

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs){

    var Time: Time = Time()
    var noOfObstaclePool = 5
    var obstacles = mutableListOf<GameObject>()

    init{
        for (i in 0 until noOfObstaclePool)
        {
            var sprite: Sprite = Sprite(100f,100f)

            //setting xPos to -1 forces spawner to respawn object at random places on start
            var rb: RigidBody = RigidBody(-1f,850f,-550f)
            var obstacle = GameObject(rb,sprite)
            obstacles.add(obstacle)
        }
    }

    private val paint = Paint().apply {
        color = Color.BLUE
    }

    private var obstaclePosition = 0f
    private val obstacleWidth = 100f

    private val animator = ValueAnimator.ofFloat(1f, 0f).apply {
        interpolator = LinearInterpolator()
        duration = 3000 // 1 second = 1000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
        addUpdateListener { animator ->
            obstaclePosition = animator.animatedValue as Float
            invalidate()
        }
    }

    private fun GameLoop(canvas: Canvas)
    {
        //Spawner
        for(i in 0 until obstacles.size)
        {
            if(obstacles[i].rigidBody.xPos < 0)
            {
                obstacles[i].rigidBody.xPos = Random.nextInt(2000,9999).toFloat()
            }
        }

        Log.d("Obstacle xPos: ", obstacles[0].rigidBody.xPos.toString())


        for(i in 0 until obstacles.size)
        {
            obstacles[i].Update(Time.deltaTime, 1)
            canvas.drawRect(obstacles[i].sprite.rectangle, obstacles[i].sprite.paint)
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        GameLoop(canvas)

    }}