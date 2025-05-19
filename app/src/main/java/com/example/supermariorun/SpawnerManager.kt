package com.example.supermariorun

import android.os.CountDownTimer
import android.widget.ImageView

class SpawnerManager(
    private val gameLogic: GameLogic,
    private val cellMatrix: Array<Array<ImageView>>,
    private val onMarioDraw: (lane: Int) -> Unit,
) {
    private val justSpawnedColumns = mutableSetOf<Int>()
    private val activeDropTimers = mutableListOf<CountDownTimer>()
    private val numRows = cellMatrix.size
    private val numCols = cellMatrix[0].size

     var isFastMode = false
        private set

    fun setFastMode(enabled: Boolean) {
        isFastMode = enabled
    }


    fun spawnBomb() {
        val type = if ((0..1).random() == 0) R.drawable.greenshell else R.drawable.redshell
        dropBomb(type)
    }

    fun spawnCoin() {
        dropCoin(R.drawable.mario_coin)
    }

    private fun dropBomb(drawableId: Int) {
        val col = (0 until numCols).shuffled().firstOrNull { it !in justSpawnedColumns }
        if (col == null) return
        justSpawnedColumns.add(col)
        var currentRow = 0
        val delay = if (isFastMode) 250L else 500L
        val timer = object : CountDownTimer(delay * numRows, delay) {
            override fun onTick(millisUntilFinished: Long) {
                if (currentRow > 0) clearCell(currentRow - 1, col)

                if (currentRow < numRows) {
                    cellMatrix[currentRow][col].setImageResource(drawableId)
                    cellMatrix[currentRow][col].tag = "shell"

                    if (currentRow == numRows - 1 && col == gameLogic.getLane()) {
                        gameLogic.loseLife()
                        onMarioDraw(gameLogic.getLane())
                    }
                }

                currentRow++
            }

            override fun onFinish() {
                clearCell(numRows - 1, col)
            }
        }.start()
        activeDropTimers.add(timer)
    }

    private fun dropCoin(drawableId: Int) {
        val col = (0 until numCols).shuffled().firstOrNull { it !in justSpawnedColumns }
        if (col == null) return
        justSpawnedColumns.add(col)
        var currentRow = 0
        val delay = if (isFastMode) 250L else 500L
        val timer = object : CountDownTimer(delay * numRows, delay) {
            override fun onTick(millisUntilFinished: Long) {
                if (currentRow > 0) clearCell(currentRow - 1, col)

                if (currentRow < numRows) {
                    val cell = cellMatrix[currentRow][col]
                    cell.setImageResource(drawableId)
                    cell.tag = "coin"

                    if (currentRow == numRows - 1 && col == gameLogic.getLane()) {
                        gameLogic.collectCoin(cell)
                        onMarioDraw(gameLogic.getLane())
                    }
                }

                currentRow++
            }

            override fun onFinish() {
                clearCell(numRows - 1, col)
            }
        }.start()
        activeDropTimers.add(timer)
    }


    private fun clearCell(row: Int, col: Int) {
        val marioLane = gameLogic.getLane()
        if (row == numRows - 1 && col == marioLane) {
            return
        }
        val cell = cellMatrix[row][col]
        cell.setImageDrawable(null)
        cell.tag = null
    }

    fun stopAll() {
        activeDropTimers.forEach { it.cancel() }
        activeDropTimers.clear()
    }
    fun clearSpawnedThisTick() {
        justSpawnedColumns.clear()
    }



}
