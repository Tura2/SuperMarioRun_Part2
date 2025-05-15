package com.example.supermariorun.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.abs

class TiltDetector(context: Context, private var tiltCallback: TiltCallback?) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private lateinit var sensorEventListener: SensorEventListener

    private var lastEventTime: Long = 0L

    init {
        initSensorLogic()
    }

    private fun initSensorLogic() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]

                Log.d("TILT_SENSOR", "x = $x, y = $y") // ← Logs raw sensor values

                val now = System.currentTimeMillis()
                if (now - lastEventTime >= 500) {
                    lastEventTime = now

                    if (abs(x) >= 3.0f) {
                        Log.d("TILT_X", "Tilt detected in X: $x")
                        tiltCallback?.tiltX(x)
                    }

                    if (abs(y) >= 3.0f) {
                        Log.d("TILT_Y", "Tilt detected in Y: $y")
                        tiltCallback?.tiltY(y)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    fun start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    fun stop() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
