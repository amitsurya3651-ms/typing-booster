package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.database.dao.TypingDao
import com.example.database.entities.AchievementEntity
import com.example.database.entities.KeyStatEntity
import com.example.database.entities.TestResultEntity
import com.example.database.entities.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        TestResultEntity::class,
        KeyStatEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun typingDao(): TypingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "typing_master_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
