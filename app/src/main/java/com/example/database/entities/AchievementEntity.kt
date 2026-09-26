package com.example.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long? = null
)
