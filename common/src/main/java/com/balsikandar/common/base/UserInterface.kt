package com.balsikandar.common.base

import io.reactivex.Observable
import io.reactivex.disposables.Disposable


// UserIntent represents any "intention of the user"
// eg. "View home screen", "Search customers with names starting with `a`", etc
interface UserIntent


// UiState is the data required to completely render any given user interface
interface UiState {

    // Partial represents some change in the current state
    interface Partial<S : UiState>
}


// UserInterface takes care of capturing user intents and rendering state
interface UserInterface<in S : UiState> {
    fun userIntents(): Observable<UserIntent>

    fun render(state: S)
}


// Presenter orchestrates a given user interface by consuming user intents and updating ui state
interface Presenter<S : UiState> {
    fun state(): Observable<S>

    fun attachIntents(intents: Observable<UserIntent>): Disposable
}