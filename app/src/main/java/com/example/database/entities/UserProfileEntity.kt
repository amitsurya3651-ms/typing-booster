package com.example.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Student",
    val currentLevel: Int = 1,
    val lessonsCompleted: Int = 0,
    val totalPracticeTimeSeconds: Long = 0L,
    val bestWpm: Int = 0,
    val averageAccuracy: Float = 0f,
    val dailyStreak: Int = 1,
    val lastPracticeDate: String = "",
    val language: String = "en", // "en" or "hi"
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val fingerColorsEnabled: Boolean = true,
    val keyboardHintsEnabled: Boolean = true,
    val autoScrollEnabled: Boolean = true
)
