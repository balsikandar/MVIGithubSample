package com.balsikandar.common.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.balsikandar.common.qualifier.UiThread
import com.balsikandar.common.utils.name
import com.balsikandar.common.utils.type
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

abstract class BaseScreen<S : UiState> : Fragment(), UserInterface<S> {

    @Inject
    @field:UiThread
    lateinit var uiScheduler: Scheduler

    @Inject
    lateinit var presenter: Presenter<S>

    private val subscriptions by lazy { CompositeDisposable() }

    private lateinit var currentState: S

    protected fun getCurrentState(): S {
        return currentState
    }

    init {
        Timber.tag("ui").d("${name()} initialized")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        //observe state
        addSubscription(
            presenter.state()
                .observeOn(uiScheduler)
                .doOnNext {
                    Timber.tag("ui/${name()}/state")
                }.subscribe {
                    this.currentState = it
                    render(it)
                }
        )

        //attach intents
        addSubscription(
            presenter.attachIntents(userIntents())
        )
    }

    override fun onPause() {
        subscriptions.clear()
        super.onPause()
        Timber.tag("ui").d("${name()} detached from ${presenter.name}")
    }

    fun addSubscription(disposable: Disposable) {
        if (!disposable.isDisposed) {
            subscriptions.add(disposable)
        }
    }

    fun name(): String = type.replace("Screen", "")

}