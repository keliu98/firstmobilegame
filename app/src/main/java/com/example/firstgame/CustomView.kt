package com.example.firstgame

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs){

//    val myLayout = findViewById<LinearLayout>(R.id.obstacle)
//    val layoutParams = LinearLayout.LayoutParams(
//        LinearLayout.LayoutParams.WRAP_CONTENT,
//        LinearLayout.LayoutParams.WRAP_CONTENT
//    )
//
//    init {
//
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        // Move the LinearLayout to the right by 10 pixels
//        layoutParams.leftMargin += 10
//        myLayout.layoutParams = layoutParams
//
//        // Invalidate the View to trigger another update
//        invalidate()
//    }

    private val paint = Paint().apply {
        color = Color.BLUE
    }

    private var obstaclePosition = 0f
    private val obstacleWidth = 100f

    private val animator = ValueAnimator.ofFloat(1f, 0f).apply {
        duration = 3000 // 1 second = 1000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
        addUpdateListener { animator ->
            obstaclePosition = animator.animatedValue as Float
            invalidate()
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

        val height = height.toFloat()
        val obstacleHeight = height / 2
        val obstacleTop = height / 2 - obstacleHeight / 2  // use constant value here
        val obstacleRect = RectF(
            width * obstaclePosition,
            obstacleTop,
            width * obstaclePosition + obstacleWidth,
            obstacleTop + obstacleHeight
        )

        canvas.drawRect(obstacleRect, paint)
    }}