package com.example.supermariorun.utilities

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

object SoundManager {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(context: Context, @RawRes soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.setVolume(0.4f, 0.4f)
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
        mediaPlayer?.start()
    }

}
