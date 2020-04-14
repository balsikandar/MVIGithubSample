package com.balsikandar.github.ui.di

import android.content.Context
import com.balsikandar.common.qualifier.IntentConsumptionThread
import com.balsikandar.common.qualifier.StateProductionThread
import com.balsikandar.common.qualifier.UiContext
import com.balsikandar.common.qualifier.UiThread
import com.balsikandar.common.scope.ScreenScope
import com.balsikandar.common.scope.UiScope
import com.balsikandar.github.MainActivity
import com.balsikandar.github.ui.trending.TrendingScreen
import com.balsikandar.github.ui.trending.di.TrendingModule
import com.google.common.util.concurrent.ThreadFactoryBuilder
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

@Module
abstract class UiModule {

    @Module
    companion object {

        @Provides
        @UiScope
        @UiContext
        @JvmStatic
        fun activityContext(activity: MainActivity): Context = activity

        @Provides
        @UiScope
        @StateProductionThread
        @JvmStatic
        fun stateScheduler(): Scheduler = Schedulers.from(
            Executors.newSingleThreadExecutor(
                ThreadFactoryBuilder().setNameFormat("state-thread").build()
            )
        )

        @Provides
        @UiScope
        @IntentConsumptionThread
        @JvmStatic
        fun userIntentScheduler(): Scheduler =
            Schedulers.from(Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("intent-thread").build()))

        @Provides
        @UiScope
        @UiThread
        @JvmStatic
        fun uiScheduler(): Scheduler = AndroidSchedulers.mainThread()
    }

    /****************************************************************
     * Screens
     ****************************************************************/
    @ScreenScope
    @ContributesAndroidInjector(modules = [TrendingModule::class])
    abstract fun homeScreen(): TrendingScreen
}