package com.balsikandar.github.data.db

import com.balsikandar.github.data.TrendingRepo

object DbEntityMapper {

    fun convert(trendingRepoAPI: TrendingRepoDB): TrendingRepo {
        return TrendingRepo(
            trendingRepoAPI.author,
            trendingRepoAPI.name,
            trendingRepoAPI.description,
            trendingRepoAPI.language,
            trendingRepoAPI.stars,
            trendingRepoAPI.forks,
            trendingRepoAPI.avatar,
            false
        )
    }
}