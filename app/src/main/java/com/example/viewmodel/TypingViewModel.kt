package com.example.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.AppDatabase
import com.example.database.entities.AchievementEntity
import com.example.database.entities.KeyStatEntity
import com.example.database.entities.TestResultEntity
import com.example.database.entities.UserProfileEntity
import com.example.engine.TypingEngine
import com.example.model.ExamParagraph
import com.example.model.Lesson
import com.example.model.LessonCatalog
import com.example.model.WeakKeyHelper
import com.example.repository.TypingRepository
import com.example.sound.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    LESSONS,
    EXAM_PARAGRAPHS,
    PRACTICE,
    RESULT,
    WEAK_KEYS,
    PROGRESS,
    SETTINGS
}

class TypingViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = TypingRepository(database.typingDao())
    val soundManager = SoundManager(application)

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val testResults: StateFlow<List<TestResultEntity>> = repository.allTestResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val keyStats: StateFlow<List<KeyStatEntity>> = repository.allKeyStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievements: StateFlow<List<AchievementEntity>> = repository.allAchievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation
    var currentScreen by mutableStateOf(Screen.HOME)
        private set

    // Active session metadata
    var activeLesson: Lesson? by mutableStateOf(null)
        private set
    var activeExamParagraph: ExamParagraph? by mutableStateOf(null)
        private set
    var activeSessionTitle: String by mutableStateOf("")
        private set
    var activeTestType: String by mutableStateOf("LESSON")
        private set
    var activeDurationSeconds: Int by mutableStateOf(0)
        private set

    // Typing engine
    val engine = TypingEngine()

    // Results & Comparison
    var latestResult: TestResultEntity? by mutableStateOf(null)
        private set
    var previousResult: TestResultEntity? by mutableStateOf(null)
        private set

    // Timer Job
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializeDefaultsIfEmpty()
        }
    }

    fun navigateTo(screen: Screen) {
        if (currentScreen == Screen.PRACTICE && screen != Screen.RESULT) {
            stopTimer()
            engine.pause()
        }
        currentScreen = screen
    }

    fun startLesson(lesson: Lesson) {
        activeLesson = lesson
        activeExamParagraph = null
        activeSessionTitle = lesson.titleEn
        activeTestType = "LESSON"
        activeDurationSeconds = lesson.defaultDurationSeconds
        engine.loadExercise(
            text = lesson.practiceText,
            durationSeconds = lesson.defaultDurationSeconds,
            allowBackspaceOption = true,
            examMode = "LESSON"
        )
        currentScreen = Screen.PRACTICE
        startTimer()
    }

    fun startTimedTest(durationSeconds: Int, title: String, text: String, allowBackspace: Boolean = true, examMode: String = "TIMED") {
        activeLesson = null
        activeExamParagraph = null
        activeSessionTitle = title
        activeTestType = when (durationSeconds) {
            60 -> "TIMED_1MIN"
            120 -> "TIMED_2MIN"
            300 -> "TIMED_5MIN"
            600 -> "TIMED_10MIN"
            900 -> "TIMED_15MIN"
            else -> "CUSTOM"
        }
        activeDurationSeconds = durationSeconds
        engine.loadExercise(
            text = text,
            durationSeconds = durationSeconds,
            allowBackspaceOption = allowBackspace,
            examMode = examMode
        )
        currentScreen = Screen.PRACTICE
        startTimer()
    }

    fun startExamParagraph(paragraph: ExamParagraph) {
        activeLesson = null
        activeExamParagraph = paragraph
        activeSessionTitle = paragraph.titleEn
        activeTestType = paragraph.examTag
        activeDurationSeconds = paragraph.durationMinutes * 60
        engine.loadExercise(
            text = paragraph.passageText,
            durationSeconds = paragraph.durationMinutes * 60,
            allowBackspaceOption = paragraph.allowBackspace,
            examMode = paragraph.examTag
        )
        currentScreen = Screen.PRACTICE
        startTimer()
    }

    fun startWeakKeyDrill(keys: List<Char>) {
        val drillText = WeakKeyHelper.generateDrillForWeakKeys(keys, wordCount = 20)
        activeLesson = null
        activeExamParagraph = null
        activeSessionTitle = "Weak Keys Drill (${keys.joinToString(", ")})"
        activeTestType = "WEAK_KEY"
        activeDurationSeconds = 0
        engine.loadExercise(drillText, 0, allowBackspaceOption = true, examMode = "WEAK_KEY")
        currentScreen = Screen.PRACTICE
        startTimer()
    }

    fun startSingleKeyDrill(char: Char) {
        val drillText = WeakKeyHelper.generateDrillForSingleKey(char)
        activeLesson = null
        activeExamParagraph = null
        activeSessionTitle = "Key '${char.uppercaseChar()}' Drill"
        activeTestType = "WEAK_KEY"
        activeDurationSeconds = 0
        engine.loadExercise(drillText, 0, allowBackspaceOption = true, examMode = "WEAK_KEY")
        currentScreen = Screen.PRACTICE
        startTimer()
    }

    fun onKeyTyped(char: Char) {
        val profile = userProfile.value
        val soundOn = profile?.soundEnabled ?: true
        val hapticsOn = profile?.hapticsEnabled ?: true

        val result = engine.processChar(char)
        if (result == true) {
            soundManager.playKeyClick(soundOn)
        } else if (result == false) {
            soundManager.playErrorTone(soundOn)
            soundManager.vibrateError(hapticsOn)
        }

        if (engine.isFinished) {
            finishSession()
        }
    }

    fun onBackspace() {
        val wasAllowed = engine.processBackspace()
        if (!wasAllowed && !engine.allowBackspace) {
            val profile = userProfile.value
            val soundOn = profile?.soundEnabled ?: true
            val hapticsOn = profile?.hapticsEnabled ?: true
            soundManager.playErrorTone(soundOn)
            soundManager.vibrateError(hapticsOn)
        }
    }

    fun togglePause() {
        if (engine.isRunning) {
            engine.pause()
        } else {
            engine.resume()
        }
    }

    fun restartSession() {
        engine.loadExercise(
            text = engine.targetText,
            durationSeconds = activeDurationSeconds,
            allowBackspaceOption = engine.allowBackspace,
            examMode = engine.examRuleMode
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                if (engine.isRunning && !engine.isFinished) {
                    engine.tickOneSecond()
                    if (engine.isFinished) {
                        finishSession()
                        break
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun finishSession() {
        stopTimer()
        engine.finish()

        val soundOn = userProfile.value?.soundEnabled ?: true
        soundManager.playSuccessTone(soundOn)

        viewModelScope.launch {
            val previousList = repository.getLatestTwoResults()
            previousResult = previousList.firstOrNull()

            val effectiveDuration = if (engine.testDurationSeconds > 0) {
                engine.testDurationSeconds - engine.remainingSeconds
            } else {
                engine.elapsedSeconds.coerceAtLeast(1)
            }

            val mistypedMap = engine.mistypedKeys.toMap()

            repository.saveCompletedTest(
                title = activeSessionTitle,
                testType = activeTestType,
                lessonId = activeLesson?.id,
                durationSeconds = effectiveDuration,
                grossWpm = engine.grossWpm,
                netWpm = engine.netWpm,
                accuracy = engine.accuracy,
                totalChars = engine.totalKeystrokes,
                correctChars = engine.correctKeystrokes,
                incorrectChars = engine.incorrectKeystrokes,
                mistypedMap = mistypedMap
            )

            latestResult = TestResultEntity(
                title = activeSessionTitle,
                testType = activeTestType,
                lessonId = activeLesson?.id,
                durationSeconds = effectiveDuration,
                grossWpm = engine.grossWpm,
                netWpm = engine.netWpm,
                accuracy = engine.accuracy,
                totalCharacters = engine.totalKeystrokes,
                correctCharacters = engine.correctKeystrokes,
                incorrectCharacters = engine.incorrectKeystrokes,
                mistypedKeysJson = mistypedMap.entries.joinToString(",") { "${it.key}:${it.value}" }
            )

            currentScreen = Screen.RESULT
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val currentLang = userProfile.value?.language ?: "en"
            val newLang = if (currentLang == "en") "hi" else "en"
            repository.updateSettings(language = newLang)
        }
    }

    fun updateSettings(
        language: String? = null,
        soundEnabled: Boolean? = null,
        hapticsEnabled: Boolean? = null,
        fingerColorsEnabled: Boolean? = null,
        keyboardHintsEnabled: Boolean? = null,
        autoScrollEnabled: Boolean? = null,
        name: String? = null
    ) {
        viewModelScope.launch {
            repository.updateSettings(
                language = language,
                soundEnabled = soundEnabled,
                hapticsEnabled = hapticsEnabled,
                fingerColorsEnabled = fingerColorsEnabled,
                keyboardHintsEnabled = keyboardHintsEnabled,
                autoScrollEnabled = autoScrollEnabled,
                name = name
            )
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.resetAllData()
            currentScreen = Screen.HOME
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        soundManager.release()
    }
}
