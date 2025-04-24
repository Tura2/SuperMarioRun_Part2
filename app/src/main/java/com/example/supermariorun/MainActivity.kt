package com.example.supermariorun
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.example.supermariorun.Utilities.SignalManager


class MainActivity : AppCompatActivity() {

    private lateinit var gameLogic: GameLogic
    private lateinit var cellMatrix: Array<Array<ImageView>>
    private lateinit var btnLeft: ImageButton
    private lateinit var btnRight: ImageButton
    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView
    private val numRows = 6
    private val numCols = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SignalManager.init(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViews()
        setupGrid()
        gameLogic = GameLogic(
            context = this,
            cellMatrix = cellMatrix,
            onMarioDraw = { lane -> drawMario(lane) },
            onHeartUpdate = { lives -> updateHearts(lives) }
        )
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
            btnLeft.setOnClickListener {
                gameLogic.moveLeft()
            }
            btnRight.setOnClickListener {
                gameLogic.moveRight()
            }
            gameLogic.drawMarioInitial()
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

    private fun drawMario(lane: Int) {
        for (i in 0 until numCols) {
            val cell = cellMatrix[numRows - 1][i]
            if (cell.tag != "shell") {
                cell.setImageDrawable(null)
                cell.tag = null
            }
        }
        cellMatrix[numRows - 1][lane].setImageResource(R.drawable.mario)
    }


    private fun startSpawningBombs() {
        object : CountDownTimer(Long.MAX_VALUE, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                gameLogic.spawnBomb()
            }

            override fun onFinish() {
                startSpawningBombs()
            }
        }.start()
    }


    private fun updateHearts(livesLeft: Int) {
        when (livesLeft) {
            2 -> heart3.setImageDrawable(null)
            1 -> heart2.setImageDrawable(null)
            0 -> heart1.setImageDrawable(null)
        }
    }


}