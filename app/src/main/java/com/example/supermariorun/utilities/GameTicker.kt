package com.example.supermariorun.utilities

import java.util.Timer
import java.util.TimerTask
import com.example.supermariorun.SpawnerManager
class GameTicker(
    private val onMeterTick: () -> Unit,
    private val onBombDrop: () -> Unit,
    private val onCoinDrop: () -> Unit,
    private val onTickEnd: () -> Unit
) {
    private var timer: Timer? = null
    private var intervalMillis: Long = NORMAL_INTERVAL
    private var tickCount = 0

    fun start() {
        stop()
        tickCount = 0
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                tickCount++

                if (tickCount % METER_TICK_INTERVAL == 0) {
                    onMeterTick()
                }
                if (tickCount % BOMB_TICK_INTERVAL == 0) {
                    onBombDrop()
                }
                if (tickCount % COIN_TICK_INTERVAL == 0) {
                    onCoinDrop()
                }
                    onTickEnd()
                }
        }, 0, intervalMillis)
    }

    fun stop() {
        timer?.cancel()
        timer = null
    }

    fun setFastMode(enabled: Boolean) {
        intervalMillis = if (enabled) FAST_INTERVAL else NORMAL_INTERVAL
        if (timer != null) {
            start()
        }
    }

    companion object {
        private const val NORMAL_INTERVAL = 100L
        private const val FAST_INTERVAL = 50L

        private const val METER_TICK_INTERVAL = 5
        private const val BOMB_TICK_INTERVAL = 10
        private const val COIN_TICK_INTERVAL = 10
    }
}
