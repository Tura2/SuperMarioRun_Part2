package com.example.supermariorun.utilities

import java.util.Timer
import java.util.TimerTask

class GameTicker(
    private val onMeterTick: () -> Unit,
    private val onBombDrop: () -> Unit,
    private val onCoinDrop: () -> Unit,
    private val onTickEnd: () -> Unit
) {
    private var timer: Timer? = null
    private var intervalMillis: Long = NORMAL_INTERVAL
    private var meterTickInterval = METER_TICK_INTERVAL_NORMAL
    private var tickCount = 0

    fun start() {
        stop()
        tickCount = 0
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                tickCount++

                if (tickCount % meterTickInterval == 0) onMeterTick()
                if (tickCount % BOMB_TICK_INTERVAL == 0) onBombDrop()
                if (tickCount % COIN_TICK_INTERVAL == 0) onCoinDrop()

                onTickEnd()
            }
        }, 0, intervalMillis)
    }

    fun stop() {
        timer?.cancel()
        timer = null
    }

    fun setFastMode(enabled: Boolean) {
        val newInterval = if (enabled) FAST_INTERVAL else NORMAL_INTERVAL
        val newMeterInterval = if (enabled) METER_TICK_INTERVAL_FAST else METER_TICK_INTERVAL_NORMAL

        if (intervalMillis != newInterval) {
            intervalMillis = newInterval
            meterTickInterval = newMeterInterval
            restart()
        }
    }

    fun setCustomInterval(newInterval: Long) {
        if (intervalMillis != newInterval) {
            intervalMillis = newInterval
            restart()
        }
    }

    private fun restart() {
        stop()
        timer = Timer()
        tickCount = 0
        timer?.schedule(object : TimerTask() {
            override fun run() {
                tickCount++

                if (tickCount % meterTickInterval == 0) onMeterTick()
                if (tickCount % BOMB_TICK_INTERVAL == 0) onBombDrop()
                if (tickCount % COIN_TICK_INTERVAL == 0) onCoinDrop()

                onTickEnd()
            }

        }, 0, intervalMillis)
    }

    companion object {
        private const val METER_TICK_INTERVAL_NORMAL = 5
        private const val METER_TICK_INTERVAL_FAST = 4

        private const val BOMB_TICK_INTERVAL = 10
        private const val COIN_TICK_INTERVAL = 10
        private const val NORMAL_INTERVAL = 100L
        private const val FAST_INTERVAL = 50L
    }
}
