package com.balsikandar.common.base

import androidx.lifecycle.ViewModel
import com.balsikandar.common.utils.type
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

abstract class BasePresenter<S : UiState, P : UiState.Partial<S>> protected constructor(
    private val initialState: S,

    private val stateThread: Scheduler,

    private val intentThread: Scheduler
) : Presenter<S>, ViewModel() {
    private val intentRelay by lazy { PublishSubject.create<UserIntent>() }

    private val stateRelay by lazy { BehaviorSubject.create<S>() }

    private val subscriptions by lazy { CompositeDisposable() }


    init {
        setupState()

        Timber.tag("ui").d("${name()} initialized")
    }

    final override fun state(): Observable<S> = stateRelay

    fun intents(): Observable<UserIntent> = intentRelay

    fun name(): String = type.replace("Presenter", "")

    final override fun attachIntents(intents: Observable<UserIntent>): Disposable {

        return intents
            .observeOn(intentThread)
            .doOnNext {
                Timber.tag("ui/${name()}/intent")
            }
            .subscribe { intentRelay.onNext(it) }
    }

    private fun setupState() {
        synchronized(this) {

            // state handling
            addSubscription(
                this.handle()
                    .observeOn(stateThread)
                    .scan(initialState) { currentState, partialState ->
                        try {
                            @Suppress("UNCHECKED_CAST")
                            val newState = reduce(currentState, partialState as P)
                            newState
                        } catch (e: Exception) {
                            Timber.e("Error updating Presenter")
                            currentState
                        }
                    }
                    .distinctUntilChanged()
                    .subscribe {
                        //synchronized to prevent a ConcurrentModificationException
                        synchronized(this) {
                            stateRelay.onNext(it)
                        }
                    }
            )
        }
    }

    override fun onCleared() {
        subscriptions.clear()
        Timber.tag("ui").d("${name()} destroyed")
    }

    protected abstract fun handle(): Observable<out UiState.Partial<S>>

    protected abstract fun reduce(currentState: S, partialState: P): S

    protected fun addSubscription(disposable: Disposable) {
        if (!disposable.isDisposed) {
            subscriptions.add(disposable)
        }
    }

    protected inline fun <reified I : UserIntent> intent(): Observable<I> {
        val intentClass = I::class.java
        return intents()
            .filter { intentClass.isAssignableFrom(it.javaClass) }
            .cast(intentClass)
    }

}