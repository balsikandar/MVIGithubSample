package com.balsikandar.github.ui.trending

import com.balsikandar.common.base.UiState
import com.balsikandar.common.base.UserIntent
import com.balsikandar.github.data.TrendingRepo

interface TrendingContract {
    data class State(
        val isLoading: Boolean = false,

        val isEmpty: Boolean = false,

        val isError: Boolean = false,

        val trendingRepos: MutableList<TrendingRepo> = mutableListOf(),

        val sortingOrder: Int = -1

    ) : UiState

    sealed class PartialState : UiState.Partial<State> {
        object ShowLoading : PartialState()

        object IsEmpty : PartialState()

        object IsError : PartialState()

        object NoChange : PartialState()

        data class SetTrendingReposList(val trendingRepos: MutableList<TrendingRepo>) :
            PartialState()
    }

    sealed class Intent : UserIntent {

        object Load : Intent()

        object NoChange : Intent()

        object Refresh : Intent()

        data class SortingOrder(val sortingOrder: Int) : Intent()
    }
}