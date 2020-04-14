package com.balsikandar.github.data

import io.reactivex.Observable

interface GithubAPI {

    fun listTrendingRepo(): Observable<MutableList<TrendingRepo>>

    fun listTrendingRepoFromServer(): Observable<MutableList<TrendingRepo>>
}

data class TrendingRepo(
    val author: String,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val avatar: String,
    var isExpanded: Boolean
)