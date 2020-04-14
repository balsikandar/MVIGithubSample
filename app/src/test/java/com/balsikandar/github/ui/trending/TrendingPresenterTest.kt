package com.balsikandar.github.ui.trending

import android.content.Context
import com.balsikandar.github.usecases.GetTrendingList
import com.balsikandar.github.usecases.GetTrendingListFromServer
import com.google.common.truth.Truth
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TrendingPresenterTest {

    lateinit var presenter: TrendingPresenter


    @field:Mock
    lateinit var getTrendingList: GetTrendingList
    @field:Mock
    lateinit var getTrendingListFromServer: GetTrendingListFromServer
    @field:Mock
    lateinit var context: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `this test should always pass`() {
        Truth.assertThat(false).isFalse()
    }

    @Test
    fun `loading trendinglist`() {
        // setup
        val initialState = TrendingContract.State()

        // create Presenter
        createPresenter(initialState)

        // observe state
        val testObserver = TestObserver<TrendingContract.State>()
        presenter.state().subscribe(testObserver)

        // provide intent (N.A.)
        presenter.attachIntents(Observable.just(TrendingContract.Intent.Load))

        // expectations
        Truth.assertThat(testObserver.values())
            .containsAnyOf(
                initialState,
                initialState.copy(isLoading = true)
            )
    }

    @Test
    fun `refresh trendinglist`() {
        // setup
        val initialState = TrendingContract.State()

        // create Presenter
        createPresenter(initialState)

        // observe state
        val testObserver = TestObserver<TrendingContract.State>()
        presenter.state().subscribe(testObserver)

        // provide intent (N.A.)
        presenter.attachIntents(Observable.just(TrendingContract.Intent.Refresh))

        // expectations
        Truth.assertThat(testObserver.values())
            .containsAnyOf(
                initialState,
                initialState.copy(isLoading = true),
                initialState.copy(isLoading = false, trendingRepos = mutableListOf())
            )
    }

    private fun createPresenter(initialState: TrendingContract.State) {
        presenter = TrendingPresenter(
            initialState = initialState,
            intentThread = Schedulers.trampoline(),
            stateThread = Schedulers.trampoline(),
            getTrendingList = getTrendingList,
            getTrendingListFromServer = getTrendingListFromServer
        )
    }
}