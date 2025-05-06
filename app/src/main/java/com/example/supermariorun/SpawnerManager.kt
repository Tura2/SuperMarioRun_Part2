package com.example.supermariorun

import android.os.CountDownTimer
import android.widget.ImageView

class SpawnerManager(
    private val gameLogic: GameLogic,
    private val cellMatrix: Array<Array<ImageView>>,
    private val onMarioDraw: (lane: Int) -> Unit,
    internal val occupiedColumns: MutableSet<Int> = mutableSetOf<Int>()

) {
    private var bombTimer: CountDownTimer? = null
    private var coinTimer: CountDownTimer? = null
    private val activeDropTimers = mutableListOf<CountDownTimer>()
    private val numRows = cellMatrix.size
    private val numCols = cellMatrix[0].size

    fun startBombs() {
        bombTimer = object : CountDownTimer(Long.MAX_VALUE, 1500L) {
            override fun onTick(millisUntilFinished: Long) {
                val col = (0 until numCols).random()
                val type = if ((0..1).random() == 0) R.drawable.greenshell else R.drawable.redshell
                dropBomb(type)
            }

            override fun onFinish() {
                startBombs()
            }
        }.start()
    }

    fun startCoins() {
        coinTimer = object : CountDownTimer(Long.MAX_VALUE, (1000L..2000L).random()) {
            override fun onTick(millisUntilFinished: Long) {
                val col = (0 until numCols).random()
                dropCoin(R.drawable.mario_coin)
            }

            override fun onFinish() {
                startCoins()
            }
        }.start()
    }

    private fun dropBomb(drawableId: Int) {
        val col = (0 until numCols).shuffled().firstOrNull { it !in occupiedColumns }
        if (col == null) return

        occupiedColumns.add(col)
        var currentRow = 0
        val delay: Long = 500L
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
                occupiedColumns.remove(col)
            }
        }.start()
        activeDropTimers.add(timer)
    }

    private fun dropCoin(drawableId: Int) {
        val col = (0 until numCols).shuffled().firstOrNull { it !in occupiedColumns }
        if (col == null) return

        occupiedColumns.add(col)
        var currentRow = 0
        val delay: Long = (400L..700L).random()
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
                occupiedColumns.remove(col)
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

    fun stopBombs() {
        bombTimer?.cancel()
    }

    fun stopCoins() {
        coinTimer?.cancel()
    }

    fun stopAll() {
        stopBombs()
        stopCoins()
        activeDropTimers.forEach { it.cancel() }
        activeDropTimers.clear()
    }

    fun startAll() {
        startBombs()
        startCoins()
    }
}
