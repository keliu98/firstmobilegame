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

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs){

    var Time: Time = Time()

    var sprite: Sprite = Sprite(100f,100f)
    var rb: RigidBody = RigidBody(2500f,850f,-550f)
    var obstacle = GameObject(rb,sprite)

    init{

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
        obstacle.Update(Time.deltaTime, 1)
        canvas.drawRect(obstacle.sprite.rectangle, obstacle.sprite.paint)
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