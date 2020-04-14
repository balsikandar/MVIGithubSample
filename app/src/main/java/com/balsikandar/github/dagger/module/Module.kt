package com.balsikandar.github.dagger.module

import com.balsikandar.common.scope.UiScope
import com.balsikandar.github.MainActivity
import com.balsikandar.github.ui.di.UiModule
import dagger.android.ContributesAndroidInjector

@dagger.Module
abstract class Module {

    @UiScope
    @ContributesAndroidInjector(modules = [UiModule::class])
    abstract fun mainActivity(): MainActivity
}