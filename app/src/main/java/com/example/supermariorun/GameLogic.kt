package com.example.supermariorun.logic

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView
import com.example.supermariorun.R
import com.example.supermariorun.Utilities.SignalManager

class GameLogic(
    private val context: Context,
    private val cellMatrix: Array<Array<ImageView>>,
    private val onMarioDraw: (lane: Int) -> Unit,
    private val onHeartUpdate: (lives: Int) -> Unit
) {
    private val numRows = cellMatrix.size
    private val numCols = cellMatrix[0].size

    private var lane = 1
    private var lives = 3

    fun moveLeft() {
        if (lane > 0) {
            lane--
            onMarioDraw(lane)
        }
    }

    fun moveRight() {
        if (lane < 2) {
            lane++
            onMarioDraw(lane)
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

                if (row > 0 && row - 1 != numRows - 1) {
                    cellMatrix[row - 1][col].setImageDrawable(null)
                }

                if (row == numRows) return

                if (row != numRows - 1 || col != lane) {
                    cellMatrix[row][col].setImageResource(drawableId)
                }

                if (row == numRows - 1 && col == lane) {
                    Log.d("GameDebug", "💥 Mario got hit!")
                    loseLife()
                }

                currentRow++
            }

            override fun onFinish() {
                if (currentRow == numRows && col != lane) {
                    cellMatrix[numRows - 1][col].setImageDrawable(null)
                }
            }
        }.start()
    }

    private fun loseLife() {
        lives--
        onHeartUpdate(lives)

        if (lives == 0) {
            SignalManager.getInstance().toast("☠️ Game Over!")
            SignalManager.getInstance().vibrate()
        }
    }

    fun drawMarioInitial() {
        onMarioDraw(lane)
    }
}
