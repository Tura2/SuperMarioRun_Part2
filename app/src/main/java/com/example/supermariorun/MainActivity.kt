package com.example.supermariorun
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.example.supermariorun.data.HighScore
import com.example.supermariorun.utilities.SignalManager
import com.example.supermariorun.utilities.UIUpdater
import com.example.supermariorun.utilities.HighScoreManager.addHighScore
import com.example.supermariorun.utilities.GameTicker
import android.view.View
import com.example.supermariorun.utilities.TiltCallback
import com.example.supermariorun.utilities.TiltDetector
import com.example.supermariorun.utilities.handleTiltX
import com.example.supermariorun.utilities.handleTiltY
import com.example.supermariorun.data.GameData


class MainActivity : AppCompatActivity(), TiltCallback {

    private lateinit var gameLogic: GameLogic
    private lateinit var cellMatrix: Array<Array<ImageView>>
    private lateinit var gameTicker: GameTicker
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
    private var useTilt: Boolean = false
    private var tiltDetector: TiltDetector? = null
    private var useSensor: Boolean = false
    private var isGameOver = false




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
        initViews()
        spawnerManager = SpawnerManager(
            gameLogic = gameLogic,
            cellMatrix = cellMatrix,
            onMarioDraw = { lane -> drawMario(lane) }
        )
        useSensor = intent.getBooleanExtra("MODE_SENSOR", false)
        useTilt = intent.getBooleanExtra("USE_TILT", false)

        if (useTilt) {
            btnLeft.visibility = View.GONE
            btnRight.visibility = View.GONE

            tiltDetector = TiltDetector(this, this)
        }
        gameTicker = GameTicker(
            onMeterTick = {
                runOnUiThread { gameLogic.incrementMeters() }
            },
            onBombDrop = {
                runOnUiThread { spawnerManager.spawnBomb() }
            },
            onCoinDrop = {
                runOnUiThread { spawnerManager.spawnCoin() }
            },
            onTickEnd = {
                runOnUiThread { spawnerManager.clearSpawnedThisTick() }
            }
        )
        gameTicker.start()
        gameTicker.setFastMode(useSensor)
        spawnerManager.setFastMode(useSensor)

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
                    lat = GameData.latitude ?: 0.0,
                    lon = GameData.longitude ?: 0.0
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
                gameTicker.start()
                gameTicker.setFastMode(useSensor)
                spawnerManager.setFastMode(useSensor)

            }
            .setNegativeButton("No") { _, _ ->
                finish()
            }
            .show()
    }

    private fun handleGameOver() {
        isGameOver = true
        gameTicker.stop()
        spawnerManager.stopAll()
        runOnUiThread {
            saveHighScore {
                showGameOverDialog()
            }
        }
    }
    override fun tiltX(direction: Float) {
        runOnUiThread {
            handleTiltX(direction, { gameLogic.moveLeft() }, { gameLogic.moveRight() })
        }
    }

    private var currentSpeedIndex = 0 // 0 = slow, 1 = fast

    override fun tiltY(y: Float) {
        currentSpeedIndex = handleTiltY(y, currentSpeedIndex) { fastMode ->
            gameTicker.setFastMode(fastMode)
            spawnerManager.setFastMode(fastMode)
        }
    }



    override fun onResume() {
        super.onResume()
        Log.d("LIFECYCLE", "onResume called - tilt start")
        if (useTilt) {
            tiltDetector?.start()
        }
    }

    override fun onPause() {
        Log.d("LIFECYCLE", "onPause called - tilt stop")
        if (useTilt) {
            tiltDetector?.stop()
        }
        super.onPause()
    }





}