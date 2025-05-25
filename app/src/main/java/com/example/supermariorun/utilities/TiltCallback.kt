package com.example.supermariorun.utilities

import android.util.Log

interface TiltCallback {
    fun tiltX(direction: Float)
    fun tiltY(y: Float)
}

fun TiltCallback.handleTiltX(direction: Float, moveLeft: () -> Unit, moveRight: () -> Unit) {
    if (direction > 0) {
        moveRight()
    } else {
        moveLeft()
    }
}

fun TiltCallback.handleTiltY(
    y: Float,
    currentSpeedIndex: Int,
    onModeChange: (Boolean) -> Unit
): Int {
    Log.d("TILT_Y", "Y-axis tilt received: $y")

    val upperThreshold = 9.5f
    val lowerThreshold = 6.0f

    val newSpeedIndex = when {
        y <= lowerThreshold && currentSpeedIndex < 1 -> currentSpeedIndex + 1
        y >= upperThreshold && currentSpeedIndex > 0 -> currentSpeedIndex - 1
        else -> currentSpeedIndex
    }

    if (newSpeedIndex != currentSpeedIndex) {
        val fastMode = newSpeedIndex == 1
        onModeChange(fastMode)
        Log.d("TILT_Y", "Y = $y → Switching to ${if (fastMode) "FAST" else "SLOW"} mode")
    } else {
        Log.d("TILT_Y", "Y = $y → No change in speed mode (already active)")
    }

    return newSpeedIndex
}
