package com.example.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val testType: String, // LESSON, TIMED_1MIN, TIMED_2MIN, TIMED_5MIN, TIMED_10MIN, WEAK_KEY
    val lessonId: Int? = null,
    val durationSeconds: Int,
    val grossWpm: Int,
    val netWpm: Int,
    val accuracy: Float,
    val totalCharacters: Int,
    val correctCharacters: Int,
    val incorrectCharacters: Int,
    val mistypedKeysJson: String = "" // key-value pairs formatted as "D:12,K:10,R:8"
)
