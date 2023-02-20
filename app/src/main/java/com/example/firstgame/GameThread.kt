package com.example.firstgame

import android.graphics.Canvas
import android.view.SurfaceHolder

/**
 * Game Thread is an active process that always runs. This is where the main game loop will keep looping.
 *
 */
class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {
    private var running: Boolean = false //sets if thread should be running, useful for pause state

    private val targetFPS = 60 // frames per second, the rate at which you would like to refresh the Canvas

    /**
     * Set if you want to run or stop.
     */
    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()

        while (running) {
            startTime = System.nanoTime()
            canvas = null

            try {
                // locking the canvas allows us to draw on to it
                canvas = this.surfaceHolder.lockCanvas()

                /**
                 * This is where the whole Game Loops!
                 */
                synchronized(surfaceHolder) {
                    this.gameView.GameLoop()
                    this.gameView.draw(canvas!!)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        /**
                         * Unlocks canvas for our use!
                         */
                        surfaceHolder.unlockCanvasAndPost(canvas)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            try {
                sleep(waitTime) //Wait until the full 1/60s is up before running game loop again.
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var canvas: Canvas? = null
    }

}