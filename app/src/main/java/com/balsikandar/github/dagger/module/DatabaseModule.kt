package com.balsikandar.github.dagger.module

import androidx.room.Room
import com.balsikandar.github.GithubApplication
import com.balsikandar.github.data.db.AppDatabase
import com.balsikandar.github.data.db.TrendingRepoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: GithubApplication): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "github.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDeviceDao(db: AppDatabase): TrendingRepoDao = db.trendingRepoDB
}