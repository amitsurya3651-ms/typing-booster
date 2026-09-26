package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.database.entities.AchievementEntity
import com.example.database.entities.KeyStatEntity
import com.example.database.entities.TestResultEntity
import com.example.database.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TypingDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // Test Results
    @Query("SELECT * FROM test_results ORDER BY timestamp DESC")
    fun getAllTestResultsFlow(): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results ORDER BY timestamp DESC LIMIT 20")
    fun getRecentTestResultsFlow(): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results ORDER BY timestamp DESC LIMIT 2")
    suspend fun getLatestTwoResults(): List<TestResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultEntity): Long

    @Query("DELETE FROM test_results")
    suspend fun clearAllTestResults()

    // Key Stats
    @Query("SELECT * FROM key_stats ORDER BY errorCount DESC")
    fun getAllKeyStatsFlow(): Flow<List<KeyStatEntity>>

    @Query("SELECT * FROM key_stats WHERE keyChar = :keyChar LIMIT 1")
    suspend fun getKeyStat(keyChar: String): KeyStatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateKeyStat(stat: KeyStatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeyStats(stats: List<KeyStatEntity>)

    @Query("DELETE FROM key_stats")
    suspend fun clearAllKeyStats()

    // Achievements
    @Query("SELECT * FROM achievements")
    fun getAllAchievementsFlow(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedTimestamp = :timestamp WHERE id = :id AND isUnlocked = 0")
    suspend fun unlockAchievement(id: String, timestamp: Long)

    @Query("UPDATE achievements SET isUnlocked = 0, unlockedTimestamp = NULL")
    suspend fun resetAllAchievements()
}
