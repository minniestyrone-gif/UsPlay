package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CoupleProfile::class,
        DateIdea::class,
        DailyChallenge::class,
        WeeklyMission::class,
        CommunityPost::class,
        PlannedDate::class
    ],
    version = 2,
    exportSchema = false
)
abstract class UsPlayDatabase : RoomDatabase() {

    abstract fun usPlayDao(): UsPlayDao

    companion object {
        @Volatile
        private var INSTANCE: UsPlayDatabase? = null

        fun getDatabase(context: Context): UsPlayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsPlayDatabase::class.java,
                    "usplay_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
