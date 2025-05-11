package com.example.supermariorun
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import com.example.supermariorun.data.HighScore
import com.example.supermariorun.utilities.HighScoreManager
import com.example.supermariorun.utilities.SignalManager
import com.example.supermariorun.utilities.UIUpdater
import com.example.supermariorun.utilities.HighScoreManager.addHighScore


class MainActivity : AppCompatActivity() {

    private lateinit var gameLogic: GameLogic
    private lateinit var cellMatrix: Array<Array<ImageView>>
    private lateinit var btnLeft: ImageButton
    private lateinit var btnRight: ImageButton
    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView
    private lateinit var metersTextView: TextView
    private lateinit var coinsTextView: TextView
    private lateinit var spawnerManager: SpawnerManager
    internal lateinit var uiUpdater: UIUpdater
    private val numRows = 9
    private val numCols = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SignalManager.init(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViews()
        setupGrid()
        uiUpdater = UIUpdater(heart1, heart2, heart3, metersTextView, coinsTextView)
        gameLogic = GameLogic(
            context = this,
            cellMatrix = cellMatrix,
            onMarioDraw = { lane -> drawMario(lane) },
            onHeartUpdate = { lives ->  uiUpdater.updateHearts(lives)},
            onMeterUpdate = { meters -> uiUpdater.updateMeters(meters) } ,
            onCoinsUpdate = { coins -> uiUpdater.updateCoins(coins) },
            onGameOver = { handleGameOver()  }
        )
        val useSensor = intent.getBooleanExtra("MODE_SENSOR", false)
        initViews()
        startGameTick()
        spawnerManager = SpawnerManager(
            gameLogic = gameLogic,
            cellMatrix = cellMatrix,
            onMarioDraw = { lane -> drawMario(lane) }
        )
        spawnerManager.startAll()
    }

    private fun findViews(){
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        heart1 = findViewById(R.id.heart1)
        heart2 = findViewById(R.id.heart2)
        heart3 = findViewById(R.id.heart3)
        metersTextView = findViewById(R.id.metersTextView)
        coinsTextView = findViewById(R.id.coinsTextView)
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
            if (cell.tag != "shell" && cell.tag != "coin") {
                cell.setImageDrawable(null)
                cell.tag = null
            }
        }
        cellMatrix[numRows - 1][lane].setImageResource(R.drawable.mario)
    }
    private fun startGameTick() {
        object : CountDownTimer(Long.MAX_VALUE, 500) {
            override fun onTick(millisUntilFinished: Long) {
                gameLogic.incrementMeters()
            }

            override fun onFinish() {
                startGameTick()
            }
        }.start()
    }
    private fun saveHighScore(onDone: () -> Unit) {
        val inputField = android.widget.EditText(this)
        inputField.hint = "Enter your name"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Game Over! Enter your name")
            .setView(inputField)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                val playerName = inputField.text.toString().ifBlank { "Anonymous" }

                val score = HighScore(
                    name = playerName,
                    coins = gameLogic.getCoins(),
                    meters = gameLogic.getMeters(),
                    lat = 32.11506,
                    lon = 34.81772
                )

                addHighScore(this, score)
                onDone()
            }
            .create()

        dialog.show()
    }

    private fun showGameOverDialog() {
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Do you want to play again?")
            .setPositiveButton("Yes") { _, _ ->
                gameLogic.reset()
                spawnerManager.startAll()
            }
            .setNegativeButton("No") { _, _ ->
                finish()
            }
            .show()
    }

    private fun handleGameOver() {
        spawnerManager.stopAll()
        spawnerManager.occupiedColumns.clear()
        runOnUiThread {
            saveHighScore {
                showGameOverDialog()
            }
        }
    }






}