package com.balsikandar.github.usecases

import com.balsikandar.common.base.Result
import com.balsikandar.common.base.UseCase
import com.balsikandar.github.data.GithubAPI
import com.balsikandar.github.data.TrendingRepo
import io.reactivex.Observable
import javax.inject.Inject

class GetTrendingList @Inject constructor(

    private val githubAPI: GithubAPI

) : UseCase<Unit, MutableList<TrendingRepo>> {

    override fun invoke(req: Unit): Observable<Result<MutableList<TrendingRepo>>> {
        return UseCase.wrapObservable(
            githubAPI.listTrendingRepo()
        )
    }
}