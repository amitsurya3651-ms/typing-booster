package com.example.repository

import com.example.database.dao.TypingDao
import com.example.database.entities.AchievementEntity
import com.example.database.entities.KeyStatEntity
import com.example.database.entities.TestResultEntity
import com.example.database.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TypingRepository(private val typingDao: TypingDao) {

    val userProfile: Flow<UserProfileEntity?> = typingDao.getUserProfileFlow()
    val allTestResults: Flow<List<TestResultEntity>> = typingDao.getAllTestResultsFlow()
    val recentTestResults: Flow<List<TestResultEntity>> = typingDao.getRecentTestResultsFlow()
    val allKeyStats: Flow<List<KeyStatEntity>> = typingDao.getAllKeyStatsFlow()
    val allAchievements: Flow<List<AchievementEntity>> = typingDao.getAllAchievementsFlow()

    suspend fun initializeDefaultsIfEmpty() {
        val currentProfile = typingDao.getUserProfile()
        if (currentProfile == null) {
            typingDao.insertOrUpdateProfile(UserProfileEntity())
        }

        // Initialize default achievements
        val defaultAchievements = listOf(
            AchievementEntity(
                id = "first_lesson",
                titleEn = "First Step",
                titleHi = "पहला कदम",
                descriptionEn = "Complete your very first typing lesson",
                descriptionHi = "अपना पहला टाइपिंग पाठ पूरा करें",
                iconName = "School"
            ),
            AchievementEntity(
                id = "speed_25",
                titleEn = "Warming Up (25 WPM)",
                titleHi = "शुरुआती गति (25 WPM)",
                descriptionEn = "Achieve typing speed of 25 WPM or higher",
                descriptionHi = "25 WPM या उससे अधिक गति प्राप्त करें",
                iconName = "Speed"
            ),
            AchievementEntity(
                id = "speed_30",
                titleEn = "Swift Typist (30 WPM)",
                titleHi = "तेज़ टाइपिस्ट (30 WPM)",
                descriptionEn = "Cross the 30 WPM threshold with confidence",
                descriptionHi = "30 WPM की सीमा को पार करें",
                iconName = "FlashOn"
            ),
            AchievementEntity(
                id = "speed_40",
                titleEn = "Speed Master (40 WPM)",
                titleHi = "स्पीड मास्टर (40 WPM)",
                descriptionEn = "Achieve a fast 40 WPM typing speed",
                descriptionHi = "40 WPM की शानदार गति हासिल करें",
                iconName = "Bolt"
            ),
            AchievementEntity(
                id = "speed_50",
                titleEn = "Keyboard Wizard (50+ WPM)",
                titleHi = "कीबोर्ड जादूगर (50+ WPM)",
                descriptionEn = "Touch-type at an elite 50+ WPM",
                descriptionHi = "50+ WPM की उत्कृष्ट गति से टाइप करें",
                iconName = "MilitaryTech"
            ),
            AchievementEntity(
                id = "accuracy_95",
                titleEn = "Precision Expert (95% Accuracy)",
                titleHi = "सटीकता विशेषज्ञ (95% Accuracy)",
                descriptionEn = "Finish a test with at least 95% accuracy",
                descriptionHi = "कम से कम 95% सटीकता के साथ टेस्ट पूरा करें",
                iconName = "CheckCircle"
            ),
            AchievementEntity(
                id = "accuracy_100",
                titleEn = "Flawless Typist (100% Accuracy)",
                titleHi = "त्रुटिरहित टाइपिस्ट (100% Accuracy)",
                descriptionEn = "Complete a test with zero errors",
                descriptionHi = "बिना किसी गलती के टेस्ट पूरा करें",
                iconName = "Stars"
            ),
            AchievementEntity(
                id = "practice_10min",
                titleEn = "Exam Ready (10-Min Test)",
                titleHi = "परीक्षा तैयार (10-मिनट टेस्ट)",
                descriptionEn = "Successfully complete a full 10-minute test",
                descriptionHi = "पूरा 10-मिनट का टाइपिंग टेस्ट समाप्त करें",
                iconName = "Timer"
            ),
            AchievementEntity(
                id = "tests_10",
                titleEn = "Dedicated Student (10 Tests)",
                titleHi = "समर्पित विद्यार्थी (10 टेस्ट)",
                descriptionEn = "Complete 10 typing tests or exercises",
                descriptionHi = "10 टाइपिंग टेस्ट या अभ्यास पूरे करें",
                iconName = "EmojiEvents"
            ),
            AchievementEntity(
                id = "streak_7",
                titleEn = "7-Day Streak",
                titleHi = "7-दिवसीय अभ्यास स्ट्रीक",
                descriptionEn = "Practice typing 7 consecutive days",
                descriptionHi = "लगातार 7 दिन टाइपिंग अभ्यास करें",
                iconName = "Whatshot"
            )
        )
        typingDao.insertInitialAchievements(defaultAchievements)
    }

    suspend fun saveCompletedTest(
        title: String,
        testType: String,
        lessonId: Int?,
        durationSeconds: Int,
        grossWpm: Int,
        netWpm: Int,
        accuracy: Float,
        totalChars: Int,
        correctChars: Int,
        incorrectChars: Int,
        mistypedMap: Map<Char, Int>
    ): Long {
        val mistypedJson = mistypedMap.entries.joinToString(",") { "${it.key}:${it.value}" }
        val testResult = TestResultEntity(
            title = title,
            testType = testType,
            lessonId = lessonId,
            durationSeconds = durationSeconds,
            grossWpm = grossWpm,
            netWpm = netWpm,
            accuracy = accuracy,
            totalCharacters = totalChars,
            correctCharacters = correctChars,
            incorrectCharacters = incorrectChars,
            mistypedKeysJson = mistypedJson
        )
        val insertedId = typingDao.insertTestResult(testResult)

        // Update key stats for weak key analysis
        val now = System.currentTimeMillis()
        for ((char, errors) in mistypedMap) {
            val keyStr = char.uppercaseChar().toString()
            val existing = typingDao.getKeyStat(keyStr)
            val updated = if (existing != null) {
                existing.copy(
                    totalAttempts = existing.totalAttempts + errors,
                    errorCount = existing.errorCount + errors,
                    lastUpdated = now
                )
            } else {
                KeyStatEntity(
                    keyChar = keyStr,
                    totalAttempts = errors,
                    correctCount = 0,
                    errorCount = errors,
                    lastUpdated = now
                )
            }
            typingDao.insertOrUpdateKeyStat(updated)
        }

        // Also record successful key attempts
        if (correctChars > 0) {
            val homeKeys = listOf("A", "S", "D", "F", "J", "K", "L", ";")
            for (key in homeKeys) {
                val existing = typingDao.getKeyStat(key)
                if (existing != null) {
                    typingDao.insertOrUpdateKeyStat(
                        existing.copy(
                            totalAttempts = existing.totalAttempts + (correctChars / 8).coerceAtLeast(1),
                            correctCount = existing.correctCount + (correctChars / 8).coerceAtLeast(1),
                            lastUpdated = now
                        )
                    )
                } else {
                    typingDao.insertOrUpdateKeyStat(
                        KeyStatEntity(
                            keyChar = key,
                            totalAttempts = 10,
                            correctCount = 10,
                            errorCount = 0,
                            lastUpdated = now
                        )
                    )
                }
            }
        }

        // Update User Profile & Streak
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(now))
        val currentProfile = typingDao.getUserProfile() ?: UserProfileEntity()
        val newStreak = calculateStreak(currentProfile.lastPracticeDate, todayStr, currentProfile.dailyStreak)
        val newLessonsCompleted = if (lessonId != null && lessonId > currentProfile.lessonsCompleted) {
            lessonId
        } else {
            currentProfile.lessonsCompleted
        }
        val newBestWpm = maxOf(currentProfile.bestWpm, netWpm)
        val newLevel = (newLessonsCompleted / 2).coerceAtLeast(1)

        val updatedProfile = currentProfile.copy(
            currentLevel = newLevel,
            lessonsCompleted = newLessonsCompleted,
            totalPracticeTimeSeconds = currentProfile.totalPracticeTimeSeconds + durationSeconds,
            bestWpm = newBestWpm,
            averageAccuracy = if (currentProfile.averageAccuracy == 0f) accuracy else (currentProfile.averageAccuracy + accuracy) / 2f,
            dailyStreak = newStreak,
            lastPracticeDate = todayStr
        )
        typingDao.insertOrUpdateProfile(updatedProfile)

        // Evaluate achievements
        evaluateAchievements(now, netWpm, accuracy, durationSeconds, lessonId, newStreak)

        return insertedId
    }

    private suspend fun evaluateAchievements(
        timestamp: Long,
        netWpm: Int,
        accuracy: Float,
        durationSeconds: Int,
        lessonId: Int?,
        streak: Int
    ) {
        if (lessonId != null && lessonId >= 1) {
            typingDao.unlockAchievement("first_lesson", timestamp)
        }
        if (netWpm >= 25) typingDao.unlockAchievement("speed_25", timestamp)
        if (netWpm >= 30) typingDao.unlockAchievement("speed_30", timestamp)
        if (netWpm >= 40) typingDao.unlockAchievement("speed_40", timestamp)
        if (netWpm >= 50) typingDao.unlockAchievement("speed_50", timestamp)
        if (accuracy >= 95f) typingDao.unlockAchievement("accuracy_95", timestamp)
        if (accuracy >= 99.5f) typingDao.unlockAchievement("accuracy_100", timestamp)
        if (durationSeconds >= 600) typingDao.unlockAchievement("practice_10min", timestamp)
        if (streak >= 7) typingDao.unlockAchievement("streak_7", timestamp)
    }

    private fun calculateStreak(lastDate: String, today: String, currentStreak: Int): Int {
        if (lastDate.isEmpty()) return 1
        if (lastDate == today) return currentStreak
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val prev = sdf.parse(lastDate)
            val curr = sdf.parse(today)
            if (prev != null && curr != null) {
                val diffDays = (curr.time - prev.time) / (1000 * 60 * 60 * 24)
                if (diffDays == 1L) currentStreak + 1 else 1
            } else 1
        } catch (_: Exception) {
            1
        }
    }

    suspend fun getLatestTwoResults(): List<TestResultEntity> {
        return typingDao.getLatestTwoResults()
    }

    suspend fun updateSettings(
        language: String? = null,
        soundEnabled: Boolean? = null,
        hapticsEnabled: Boolean? = null,
        fingerColorsEnabled: Boolean? = null,
        keyboardHintsEnabled: Boolean? = null,
        autoScrollEnabled: Boolean? = null,
        name: String? = null
    ) {
        val profile = typingDao.getUserProfile() ?: UserProfileEntity()
        val updated = profile.copy(
            language = language ?: profile.language,
            soundEnabled = soundEnabled ?: profile.soundEnabled,
            hapticsEnabled = hapticsEnabled ?: profile.hapticsEnabled,
            fingerColorsEnabled = fingerColorsEnabled ?: profile.fingerColorsEnabled,
            keyboardHintsEnabled = keyboardHintsEnabled ?: profile.keyboardHintsEnabled,
            autoScrollEnabled = autoScrollEnabled ?: profile.autoScrollEnabled,
            name = name ?: profile.name
        )
        typingDao.insertOrUpdateProfile(updated)
    }

    suspend fun resetAllData() {
        typingDao.clearAllTestResults()
        typingDao.clearAllKeyStats()
        typingDao.resetAllAchievements()
        typingDao.insertOrUpdateProfile(UserProfileEntity())
    }
}
