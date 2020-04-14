package com.balsikandar.github.data.network

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("repositories")
    fun getTrendingRepos(): Single<Response<MutableList<ApiMessages.TrendingRepoAPI>>>
}