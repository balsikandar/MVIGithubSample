package com.balsikandar.github.ui.trending.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.balsikandar.common.base.Presenter
import com.balsikandar.common.qualifier.InitialState
import com.balsikandar.common.scope.ScreenScope
import com.balsikandar.github.ui.trending.TrendingContract
import com.balsikandar.github.ui.trending.TrendingPresenter
import com.balsikandar.github.ui.trending.TrendingScreen
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
abstract class TrendingModule {

    @Module
    companion object {
        @Provides
        @ScreenScope
        @InitialState
        @JvmStatic
        fun initialState(): TrendingContract.State = TrendingContract.State()

        @Provides
        @ScreenScope
        @JvmStatic
        fun presenter(
            fragment: TrendingScreen,
            presenterProvider: Provider<TrendingPresenter>
        ): Presenter<TrendingContract.State> =
            ViewModelProviders.of(
                fragment,
                object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                        presenterProvider.get() as T
                }
            )[TrendingPresenter::class.java]
    }
}