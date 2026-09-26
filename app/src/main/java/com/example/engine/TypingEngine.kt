package com.example.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.model.Finger
import kotlin.math.max
import kotlin.math.roundToInt

sealed class TypedCharState {
    object Pending : TypedCharState()
    object Correct : TypedCharState()
    data class Incorrect(val typed: Char) : TypedCharState()
}

data class KeystrokeError(
    val expected: Char,
    val pressed: Char,
    val timestamp: Long = System.currentTimeMillis()
)

class TypingEngine {

    var targetText: String by mutableStateOf("")
        private set

    var currentIndex: Int by mutableIntStateOf(0)
        private set

    val charStates = mutableStateListOf<TypedCharState>()

    var totalKeystrokes: Int by mutableIntStateOf(0)
        private set

    var correctKeystrokes: Int by mutableIntStateOf(0)
        private set

    var incorrectKeystrokes: Int by mutableIntStateOf(0)
        private set

    // Maps expected character -> count of mistakes
    val mistypedKeys = mutableStateMapOf<Char, Int>()
    val errorLog = mutableStateListOf<KeystrokeError>()

    // Timing
    var isRunning: Boolean by mutableStateOf(false)
        private set

    var isFinished: Boolean by mutableStateOf(false)
        private set

    var testDurationSeconds: Int by mutableIntStateOf(0)
        private set

    var remainingSeconds: Int by mutableIntStateOf(0)
        private set

    var elapsedSeconds: Int by mutableIntStateOf(0)
        private set

    // Calculated metrics
    var grossWpm: Int by mutableIntStateOf(0)
        private set

    var netWpm: Int by mutableIntStateOf(0)
        private set

    var accuracy: Float by mutableFloatStateOf(100f)
        private set

    // Exam & Backspace Rules
    var allowBackspace: Boolean by mutableStateOf(true)
        private set

    var blockedBackspaceCount: Int by mutableIntStateOf(0)
        private set

    var examRuleMode: String by mutableStateOf("NORMAL")
        private set

    val currentTargetChar: Char?
        get() = if (currentIndex < targetText.length) targetText[currentIndex] else null

    val currentTargetFinger: Finger?
        get() = currentTargetChar?.let { Finger.getFingerForChar(it) }

    fun loadExercise(
        text: String,
        durationSeconds: Int = 0,
        allowBackspaceOption: Boolean = true,
        examMode: String = "NORMAL"
    ) {
        targetText = text.trim()
        currentIndex = 0
        totalKeystrokes = 0
        correctKeystrokes = 0
        incorrectKeystrokes = 0
        grossWpm = 0
        netWpm = 0
        accuracy = 100f
        testDurationSeconds = durationSeconds
        remainingSeconds = durationSeconds
        elapsedSeconds = 0
        allowBackspace = allowBackspaceOption
        blockedBackspaceCount = 0
        examRuleMode = examMode
        isRunning = false
        isFinished = false
        mistypedKeys.clear()
        errorLog.clear()
        charStates.clear()
        for (i in targetText.indices) {
            charStates.add(TypedCharState.Pending)
        }
    }

    fun start() {
        if (!isFinished) {
            isRunning = true
        }
    }

    fun pause() {
        isRunning = false
    }

    fun resume() {
        if (!isFinished) {
            isRunning = true
        }
    }

    /**
     * Process an incoming key press.
     * Returns true if correct, false if incorrect or error, null if no-op.
     */
    fun processChar(inputChar: Char): Boolean? {
        if (isFinished || targetText.isEmpty()) return null

        if (!isRunning) {
            start()
        }

        if (currentIndex >= targetText.length) {
            finish()
            return null
        }

        val expectedChar = targetText[currentIndex]
        totalKeystrokes++

        val isMatch = (inputChar == expectedChar)

        if (isMatch) {
            charStates[currentIndex] = TypedCharState.Correct
            correctKeystrokes++
            currentIndex++
        } else {
            charStates[currentIndex] = TypedCharState.Incorrect(inputChar)
            incorrectKeystrokes++
            val expectedUpper = expectedChar.uppercaseChar()
            mistypedKeys[expectedUpper] = (mistypedKeys[expectedUpper] ?: 0) + 1
            errorLog.add(KeystrokeError(expected = expectedChar, pressed = inputChar))
            // Auto advance so student isn't stuck forever, but mistake is recorded
            currentIndex++
        }

        recalculateStats()

        if (currentIndex >= targetText.length) {
            finish()
        }

        return isMatch
    }

    fun processBackspace(): Boolean {
        if (!allowBackspace) {
            blockedBackspaceCount++
            return false
        }
        if (isFinished || currentIndex <= 0) return false
        currentIndex--
        val previousState = charStates[currentIndex]
        if (previousState is TypedCharState.Incorrect) {
            incorrectKeystrokes = max(0, incorrectKeystrokes - 1)
        } else if (previousState is TypedCharState.Correct) {
            correctKeystrokes = max(0, correctKeystrokes - 1)
        }
        charStates[currentIndex] = TypedCharState.Pending
        recalculateStats()
        return true
    }

    fun tickOneSecond() {
        if (!isRunning || isFinished) return

        elapsedSeconds++

        if (testDurationSeconds > 0) {
            remainingSeconds = max(0, remainingSeconds - 1)
            if (remainingSeconds <= 0) {
                finish()
            }
        }

        recalculateStats()
    }

    private fun recalculateStats() {
        val effectiveTimeSeconds = if (testDurationSeconds > 0) {
            elapsedSeconds.coerceAtLeast(1)
        } else {
            elapsedSeconds.coerceAtLeast(1)
        }

        val minutes = effectiveTimeSeconds / 60.0

        if (minutes > 0) {
            // Standard formula: WPM = (Keystrokes / 5) / minutes
            val gross = ((totalKeystrokes / 5.0) / minutes).roundToInt()
            grossWpm = max(0, gross)

            // Net WPM = ((Correct Keystrokes / 5.0) / minutes)
            val net = ((correctKeystrokes / 5.0) / minutes).roundToInt()
            netWpm = max(0, net)
        } else {
            grossWpm = 0
            netWpm = 0
        }

        accuracy = if (totalKeystrokes > 0) {
            ((correctKeystrokes.toFloat() / totalKeystrokes) * 100f).coerceIn(0f, 100f)
        } else {
            100f
        }
    }

    fun finish() {
        isRunning = false
        isFinished = true
        recalculateStats()
    }
}
