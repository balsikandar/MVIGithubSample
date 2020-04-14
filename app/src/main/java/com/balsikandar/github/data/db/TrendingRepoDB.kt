package com.balsikandar.github.data.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TrendingRepo")
data class TrendingRepoDB(
    @PrimaryKey
    @NonNull
    val author: String,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val avatar: String
)