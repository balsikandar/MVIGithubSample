package com.balsikandar.github.dagger.component

import com.balsikandar.github.GithubApplication
import com.balsikandar.github.dagger.module.DatabaseModule
import com.balsikandar.github.dagger.module.Module
import com.balsikandar.github.dagger.module.NetworkModule
import com.balsikandar.github.data.di.GithubSdk
import com.balsikandar.github.usecases.UseCases
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Scope
@Singleton
@Component(
    modules = [
        Module::class,
        NetworkModule::class,
        DatabaseModule::class,
        UseCases.Module::class,
        GithubSdk.Module::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(githubApplication: GithubApplication): Builder

        fun networkModule(networkModule: NetworkModule): Builder

        fun build(): AppComponent
    }

    fun inject(githubApplication: GithubApplication)
}

@javax.inject.Scope
annotation class Scope