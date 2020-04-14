package com.balsikandar.github.data.sync

import com.balsikandar.github.data.GithubAPI
import com.balsikandar.github.data.TrendingRepo
import com.balsikandar.github.data.network.TrendingRepository
import io.reactivex.Observable
import javax.inject.Inject

class SyncGithubApiImpl @Inject constructor(

    private val trendingRepository: TrendingRepository

) : GithubAPI {
    override fun listTrendingRepo(): Observable<MutableList<TrendingRepo>> {
        return trendingRepository.getTrendingRepos()
    }

    override fun listTrendingRepoFromServer(): Observable<MutableList<TrendingRepo>> {
        return trendingRepository.getTrendingReposFromServer()
    }

}

interface IServer {
    fun getTrendingRepos(): Observable<MutableList<TrendingRepo>>

    fun getTrendingReposFromServer(): Observable<MutableList<TrendingRepo>>
}