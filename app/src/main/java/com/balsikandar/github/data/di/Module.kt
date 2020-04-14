package com.balsikandar.github.data.di

import com.balsikandar.github.data.GithubAPI
import com.balsikandar.github.data.network.TrendingRepository
import com.balsikandar.github.data.sync.IServer
import com.balsikandar.github.data.sync.SyncGithubApiImpl
import dagger.Binds

object GithubSdk {

    @dagger.Module
    abstract class Module {

        @Binds
        abstract fun api(apiImpl: SyncGithubApiImpl): GithubAPI

        @Binds
        abstract fun server(trendingRepositoryAPI: TrendingRepository): IServer

//        @Binds
//        abstract fun apiClient(apiClient: ApiClient):
    }
}