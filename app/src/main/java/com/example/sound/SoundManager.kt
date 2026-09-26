package com.example.sound

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View

class SoundManager(private val context: Context) {

    private var toneGenerator: ToneGenerator? = null
    private var vibrator: Vibrator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 60)
        } catch (_: Exception) {
            toneGenerator = null
        }

        try {
            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Exception) {
            vibrator = null
        }
    }

    fun playKeyClick(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 25)
        } catch (_: Exception) {
            // Safe fallback
        }
    }

    fun playErrorTone(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 60)
        } catch (_: Exception) {
            // Safe fallback
        }
    }

    fun playSuccessTone(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
        } catch (_: Exception) {
            // Safe fallback
        }
    }

    fun vibrateError(hapticsEnabled: Boolean) {
        if (!hapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(40)
            }
        } catch (_: Exception) {
            // Safe fallback
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {}
    }
}
