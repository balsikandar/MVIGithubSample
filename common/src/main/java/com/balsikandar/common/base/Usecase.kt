package com.balsikandar.common.base

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Qualifier

sealed class Result<T> {
    data class Progress<T>(val value: Int = 0, val maxValue: Int = 100) : Result<T>()
    data class Success<T>(val value: T) : Result<T>()
    data class Failure<T>(val error: Throwable) : Result<T>()
}

interface UseCase<RequestT, ResponseT> {
    operator fun invoke(req: RequestT): Observable<Result<ResponseT>>

    companion object {

        fun <ResponseT> wrapObservable(s: Observable<ResponseT>): Observable<Result<ResponseT>> {
            return s.map<Result<ResponseT>> { Result.Success(it) }
                .startWith(Result.Progress())
                .onErrorReturn { Result.Failure(it) }
        }

        fun <ResponseT> wrapSingle(s: Single<ResponseT>): Observable<Result<ResponseT>> {
            return wrapObservable<ResponseT>(s.flatMapObservable<ResponseT> { Observable.just(it) })
        }

        fun wrapCompletable(c: Completable): Observable<Result<Unit>> {
            return wrapSingle(c.toSingleDefault(Unit))
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UseCase_(val name: String)