package com.example.supermariorun

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.example.supermariorun.utilities.SignalManager

class GameLogic(
    private val context: Context,
    private val cellMatrix: Array<Array<ImageView>>,
    private val onMarioDraw: (lane: Int) -> Unit,
    private val onHeartUpdate: (lives: Int) -> Unit,
    private val onMeterUpdate: (metersPassed: Int) -> Unit,
    private val onCoinsUpdate: (coinsCollected: Int) -> Unit = {},
    private val onGameOver: () -> Unit
) {
    private val numRows = cellMatrix.size
    internal var metersPassed = 0
    internal var coinsCollected = 0
    private var lane = 2
    private var lives = 3
    private var isGameOver = false
    fun getLane(): Int = lane

    fun moveLeft() {
        if (lane > 0) {
            lane--
            onMarioDraw(lane)
            checkForCollision()
        }
    }

    fun moveRight() {
        if (lane <  cellMatrix[0].size - 1 ) {
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
        if (cell.tag == "coin") {
            Log.d("GameDebug", "💰 Mario collected a coin!")
            collectCoin(cell)
        }
    }

    fun incrementMeters() {
        if (isGameOver) return
        metersPassed++
        (context as MainActivity).runOnUiThread {
            onMeterUpdate(metersPassed)
        }
    }


    public fun collectCoin(cell: ImageView) {
        coinsCollected++
        onMeterUpdate(metersPassed)
        (context as MainActivity).runOnUiThread {
            context.uiUpdater.updateCoins(coinsCollected)
        }
        cell.setImageDrawable(null)
        cell.tag = null
        onMarioDraw(lane)
    }

    public fun loseLife() {
        if (isGameOver) return
        lives--
        onHeartUpdate(lives)

        val marioCell = cellMatrix[numRows - 1][lane]
        marioCell.setImageDrawable(null)
        marioCell.tag = null
        onMarioDraw(lane)

        if (lives == 0) {
            isGameOver = true
            SignalManager.getInstance().toast("☠️ Game Over!")
            SignalManager.getInstance().vibrate()
            onGameOver()
        }
    }
    fun reset() {
        for (row in 0 until numRows) {
            for (col in 0 until cellMatrix[0].size) {
                val cell = cellMatrix[row][col]
                cell.setImageDrawable(null)
                cell.tag = null
            }
        }
        lane = 2
        lives = 3
        metersPassed = 0
        coinsCollected = 0
        isGameOver = false
        onHeartUpdate(lives)
        onMeterUpdate(metersPassed)
        onCoinsUpdate(coinsCollected)
        onMarioDraw(lane)


    }


    fun drawMarioInitial() {
        onMarioDraw(lane)
    }

}
