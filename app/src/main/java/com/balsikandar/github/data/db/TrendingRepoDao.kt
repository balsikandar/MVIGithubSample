package com.balsikandar.github.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface TrendingRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg trendingRepoDB: TrendingRepoDB)

    @Query("DELETE FROM TrendingRepo")
    fun deleteAll();

    @Query("SELECT * FROM TrendingRepo ORDER BY author DESC")
    fun trendingRepoList(): Flowable<List<TrendingRepoDB>>
}