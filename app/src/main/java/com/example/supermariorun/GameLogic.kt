package com.example.supermariorun

import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.example.supermariorun.Utilities.SignalManager

class GameLogic(
    private val context: Context,
    private val cellMatrix: Array<Array<ImageView>>,
    private val onMarioDraw: (lane: Int) -> Unit,
    private val onHeartUpdate: (lives: Int) -> Unit
) {
    private val numRows = cellMatrix.size

    private var lane = 1
    private var lives = 3

    fun moveLeft() {
        if (lane > 0) {
            lane--
            onMarioDraw(lane)
            checkForCollision()
        }
    }

    fun moveRight() {
        if (lane < 2) {
            lane++
            onMarioDraw(lane)
            checkForCollision()
        }
    }
    private fun checkForCollision() {
        val cell = cellMatrix[numRows - 1][lane]
        Log.d("Debug", "Mario is on tag: ${cell.tag}")
        if (cell.tag == "shell") {
            Log.d("GameDebug", "💥 Mario walked into a shell!")
            cell.setImageDrawable(null)
            cell.tag = null
            loseLife()
        }
    }


    fun spawnBomb() {
        val col = (0..2).random()
        val type = if ((0..1).random() == 0) R.drawable.greenshell else R.drawable.redshell
        dropBomb(col, type)
    }

    private fun dropBomb(col: Int, drawableId: Int) {
        var currentRow = 0
        val delay: Long = 500L

        object : CountDownTimer(delay * numRows, delay) {
            override fun onTick(millisUntilFinished: Long) {
                val row = currentRow

                // Clear previous shell cell
                if (row > 0) {
                    cellMatrix[row - 1][col].setImageDrawable(null)
                    cellMatrix[row - 1][col].tag = null
                }

                if (row >= numRows) return

                // Draw the shell and set tag
                cellMatrix[row][col].setImageResource(drawableId)
                cellMatrix[row][col].tag = "shell"

                // Hit from above
                if (row == numRows - 1 && col == lane) {
                    loseLife()
                }

                currentRow++
            }

            override fun onFinish() {
                // Clear shell if still there
                if (currentRow == numRows && col != lane) {
                    cellMatrix[numRows - 1][col].setImageDrawable(null)
                    cellMatrix[numRows - 1][col].tag = null
                }
            }
        }.start()
    }


    private fun loseLife() {
        lives--
        onHeartUpdate(lives)

        val marioCell = cellMatrix[numRows - 1][lane]
        marioCell.setImageDrawable(null)
        marioCell.tag = null
        onMarioDraw(lane)

        if (lives == 0) {
            SignalManager.getInstance().toast("☠️ Game Over!")
            SignalManager.getInstance().vibrate()
        }
    }

    fun drawMarioInitial() {
        onMarioDraw(lane)
    }
}
