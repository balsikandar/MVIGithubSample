package com.balsikandar.github.data.network

import com.balsikandar.github.data.db.TrendingRepoDB

object ApiEntityMapper {

    fun convert(trendingRepoAPI: ApiMessages.TrendingRepoAPI): TrendingRepoDB {
        return TrendingRepoDB(
            trendingRepoAPI.author,
            trendingRepoAPI.name,
            trendingRepoAPI.description,
            trendingRepoAPI.language,
            trendingRepoAPI.stars,
            trendingRepoAPI.forks,
            trendingRepoAPI.avatar
        )
    }
}