package com.example.firstgame

import java.util.*

class GameScoreManager {
    private var score = 0
    private var isRunning = false
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var timerRunnable: TimerTask? = null

    private val timer = Timer()

    fun start() {
        if (!isRunning) {
            isRunning = true
            startTime = System.currentTimeMillis()
            timerRunnable = object : TimerTask() {
                override fun run() {
                    elapsedTime = System.currentTimeMillis() - startTime
                }
            }
            timer.schedule(timerRunnable, 0, 1000)
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            elapsedTime += System.currentTimeMillis() - startTime
            timerRunnable?.cancel()
            timerRunnable = null
        }
    }

    fun addScore(points: Int) {
        score += points
    }

    fun getScore(): Int {
        return score
    }

    fun getElapsedTime(): Long {
        return elapsedTime
    }
}

