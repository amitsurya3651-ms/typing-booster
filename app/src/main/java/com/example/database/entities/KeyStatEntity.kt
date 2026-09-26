package com.example.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_stats")
data class KeyStatEntity(
    @PrimaryKey val keyChar: String, // e.g. "D", "K", "A"
    val totalAttempts: Int = 0,
    val correctCount: Int = 0,
    val errorCount: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    val accuracyPercent: Int
        get() = if (totalAttempts > 0) ((correctCount.toFloat() / totalAttempts) * 100).toInt() else 100
}
