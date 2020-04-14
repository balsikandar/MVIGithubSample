package com.balsikandar.github.data.network

import android.content.SharedPreferences
import com.balsikandar.github.data.TrendingRepo
import com.balsikandar.github.data.db.DbEntityMapper
import com.balsikandar.github.data.db.TrendingRepoDB
import com.balsikandar.github.data.db.TrendingRepoDao
import com.balsikandar.github.data.sync.IServer
import com.balsikandar.github.usecases.HasTwoHoursPassed
import com.balsikandar.github.utils.ThreadUtils
import io.reactivex.Observable
import javax.inject.Inject

class TrendingRepository @Inject constructor(
    private val trendingRepoDao: TrendingRepoDao,
    private val sharedPreferences: SharedPreferences,
    private val apiService: ApiService
) : IServer {

    override fun getTrendingRepos(): Observable<MutableList<TrendingRepo>> {

        if (HasTwoHoursPassed().execute(getLastCallTime())) {
            return getTrendingReposFromServer()
        }

        if (trendingRepoDao.trendingRepoList().blockingFirst().isEmpty()) {
            return getTrendingReposFromServer()
        }

        return trendingRepoDao.trendingRepoList()
            .subscribeOn(ThreadUtils.database())
            .observeOn(ThreadUtils.worker())
            .map {
                val repoList = mutableListOf<TrendingRepo>()
                for (repo in it) {
                    repoList.add(DbEntityMapper.convert(repo))
                }
                return@map repoList
            }.toObservable()
    }

    private fun getLastCallTime(): Long {
        val lastCallTime = sharedPreferences.getLong("last_call_time", 0L)
        return lastCallTime
    }

    override fun getTrendingReposFromServer(): Observable<MutableList<TrendingRepo>> {
        return apiService.getTrendingRepos()
            .subscribeOn(ThreadUtils.api())
            .observeOn(ThreadUtils.worker())
            .map {
                if (it.isSuccessful) {
                    return@map it.body()
                } else {
                    throw Error.parse(it)
                }
            }.map {
                val trendingReposDB = mutableListOf<TrendingRepoDB>()
                for (repo in it) {
                    trendingReposDB.add(ApiEntityMapper.convert(repo))
                }
                trendingRepoDao.insert(*trendingReposDB.toTypedArray())
                val repoList = mutableListOf<TrendingRepo>()
                for (repo in trendingReposDB) {
                    repoList.add(DbEntityMapper.convert(repo))
                }
                sharedPreferences.edit().putLong("last_call_time", System.currentTimeMillis())
                    .apply()
                return@map repoList
            }.toObservable()
    }

}