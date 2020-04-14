package com.balsikandar.github.ui.trending

import com.balsikandar.common.base.BasePresenter
import com.balsikandar.common.base.Result
import com.balsikandar.common.base.UiState
import com.balsikandar.common.qualifier.InitialState
import com.balsikandar.common.qualifier.IntentConsumptionThread
import com.balsikandar.common.qualifier.StateProductionThread
import com.balsikandar.github.data.TrendingRepo
import com.balsikandar.github.usecases.GetTrendingList
import com.balsikandar.github.usecases.GetTrendingListFromServer
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import javax.inject.Inject

class TrendingPresenter @Inject constructor(
    @InitialState initialState: TrendingContract.State,
    @StateProductionThread stateThread: Scheduler,
    @IntentConsumptionThread intentThread: Scheduler,
    private val getTrendingList: GetTrendingList,
    private val getTrendingListFromServer: GetTrendingListFromServer
) : BasePresenter<TrendingContract.State, TrendingContract.PartialState>(
    initialState,
    stateThread,
    intentThread
) {

    private lateinit var repos: MutableList<TrendingRepo>

    override fun handle(): Observable<out UiState.Partial<TrendingContract.State>> {
        return Observable.mergeArray(
            intent<TrendingContract.Intent.Load>()
                .doOnNext {
                    Timber.e("Load: Presenter")
                }
                .switchMap { getTrendingList.invoke(Unit) }
                .map {
                    when (it) {
                        is Result.Progress -> TrendingContract.PartialState.ShowLoading
                        is Result.Success -> {
                            repos = it.value
                            TrendingContract.PartialState.SetTrendingReposList(it.value)
                        }

                        is Result.Failure -> {
                            TrendingContract.PartialState.IsError
                        }
                    }
                },

            intent<TrendingContract.Intent.Refresh>()
                .switchMap { getTrendingListFromServer.invoke(Unit) }
                .map {
                    when (it) {
                        is Result.Progress -> TrendingContract.PartialState.ShowLoading
                        is Result.Success -> {
                            repos = it.value
                            TrendingContract.PartialState.SetTrendingReposList(it.value)
                        }

                        is Result.Failure -> {
                            TrendingContract.PartialState.IsError
                        }
                    }
                },


            intent<TrendingContract.Intent.SortingOrder>()
                .map {
                    var mRepos = mutableListOf<TrendingRepo>()
                    if (it.sortingOrder == 1) {
                        mRepos = repos.sortedWith(compareBy(TrendingRepo::stars)).toMutableList()
                    } else {
                        mRepos = repos.sortedWith(compareBy(TrendingRepo::author)).toMutableList()
                    }
                    TrendingContract.PartialState.SetTrendingReposList(mRepos)
                }

        )
    }

    override fun reduce(
        currentState: TrendingContract.State,
        partialState: TrendingContract.PartialState
    ): TrendingContract.State {
        return when (partialState) {
            is TrendingContract.PartialState.ShowLoading -> currentState.copy(isLoading = true)
            is TrendingContract.PartialState.IsEmpty -> currentState.copy(
                trendingRepos = mutableListOf(),
                isLoading = false
            )
            is TrendingContract.PartialState.IsError -> currentState.copy(
                isError = true,
                isLoading = false
            )
            is TrendingContract.PartialState.NoChange -> currentState
            is TrendingContract.PartialState.SetTrendingReposList -> currentState.copy(
                isLoading = false,
                trendingRepos = partialState.trendingRepos
            )
        }
    }

}