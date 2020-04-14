package com.balsikandar.github.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TrendingRepoDB::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val trendingRepoDB: TrendingRepoDao
}