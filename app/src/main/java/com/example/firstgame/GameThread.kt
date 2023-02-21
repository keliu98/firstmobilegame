package com.example.firstgame

import android.graphics.Canvas
import android.view.SurfaceHolder

/**
 * Game Thread is an active process that always runs. This is where the main game loop will keep looping.
 *
 */
class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {
    private var running: Boolean = false //sets if thread should be running, useful for pause state

    var Time = Time()

    /**
     * Set if you want to run or stop.
     */
    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    override fun run() {


        while (running) {
            Time.startTime = System.nanoTime()
            canvas = null

            try {
                // locking the canvas allows us to draw on to it
                canvas = this.surfaceHolder.lockCanvas()

                /**
                 * This is where the whole Game Loops!
                 */
                synchronized(surfaceHolder) {
                    this.gameView.GameLoop(Time.targetDeltaTime,Time.step)
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

            Time.endTime = System.nanoTime()
            Time.Update() //Calculates the correct steps for simulation to take
        }
    }

    companion object {
        private var canvas: Canvas? = null
    }

}