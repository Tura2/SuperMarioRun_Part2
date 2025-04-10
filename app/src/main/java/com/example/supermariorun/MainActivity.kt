package com.example.supermariorun
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge

class MainActivity : AppCompatActivity() {

    private lateinit var cellMatrix: Array<Array<ImageView>>
    private lateinit var btnLeft: ImageButton
    private lateinit var btnRight: ImageButton
    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView
    private var lives = 3
    private var lane = 1  // 0 = left, 1 = center, 2 = right
    private val numRows = 6
    private val numCols = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViews()
        setupGrid()
        initViews()
        startSpawningBombs()
    }
    private fun findViews(){
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        heart1 = findViewById(R.id.heart1)
        heart2 = findViewById(R.id.heart2)
        heart3 = findViewById(R.id.heart3)
    }
    private fun initViews() {
        btnLeft.setOnClickListener { if (lane > 0) { lane--
            drawMario() }
        }
        btnRight.setOnClickListener { if (lane < 2) { lane++
                drawMario() }
        }
        drawMario()
    }

    private fun spawnBomb() {
        val col = (0..2).random()
        val type = if ((0..1).random() == 0) R.drawable.greenshell else R.drawable.redshell

        dropBomb(col, type)

    }


    private fun setupGrid() {
        val gridLayout = findViewById<GridLayout>(R.id.grid)
        gridLayout.rowCount = numRows
        gridLayout.columnCount = numCols

        cellMatrix = Array(numRows) { row ->
            Array(numCols) { col ->
                val imageView = ImageView(this)
                imageView.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(row, 1f)
                    columnSpec = GridLayout.spec(col, 1f)
                }
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                gridLayout.addView(imageView)
                imageView
            }
        }
    }

    private fun drawMario() {
        // Clear bottom row first
        for (i in 0 until numCols) {
            cellMatrix[numRows - 1][i].setImageDrawable(null)
        }
        // Set Mario in the current lane
        cellMatrix[numRows - 1][lane].setImageResource(R.drawable.mario)
    }

    private fun startSpawningBombs() {
        object : CountDownTimer(Long.MAX_VALUE, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                spawnBomb()
            }

            override fun onFinish() {
                startSpawningBombs()
            }
        }.start()
    }


    private fun dropBomb(col: Int, drawableId: Int) {
        var currentRow = 0
        val delay: Long = 500L // drop speed

        object : CountDownTimer(delay * numRows, delay) {
            override fun onTick(millisUntilFinished: Long) {
                val row = currentRow

                // Clear previous position
                if (row > 0 && row - 1 != numRows - 1) {
                    cellMatrix[row - 1][col].setImageDrawable(null)
                }

                // Reached bottom
                if (row == numRows) return

                // Draw the shell
                if (row != numRows - 1 || col != lane) {
                    // Normal draw
                    cellMatrix[row][col].setImageResource(drawableId)
                }

                // COLLISION
                if (row == numRows - 1 && col == lane) {
                    Log.d("GameDebug", "💥 Mario got hit!")
                    loseLife()
                }

                currentRow++
            }

            override fun onFinish() {
                // Clear final shell if it's not overlapping Mario
                if (currentRow == numRows && col != lane) {
                    cellMatrix[numRows - 1][col].setImageDrawable(null)
                }
            }
        }.start()
    }


    private fun loseLife() {
        lives--
        when (lives) {
            2 -> heart3.setImageDrawable(null)
            1 -> heart2.setImageDrawable(null)
            0 -> {
                heart1.setImageDrawable(null)
                Toast.makeText(this, "☠️ Game Over!", Toast.LENGTH_LONG).show()
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                )

            }

        }
    }
}