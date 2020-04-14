package com.balsikandar.github.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

object ThreadUtils {
    private val DATABASE = Schedulers.from(Executors.newCachedThreadPool())
    private val API = Schedulers.from(Executors.newCachedThreadPool())
    private val DOWNLOAD = Schedulers.from(Executors.newFixedThreadPool(2))
    private val UPLOAD = Schedulers.from(Executors.newFixedThreadPool(2))
    private val FILES = Schedulers.from(Executors.newCachedThreadPool())
    private val WORKER = Schedulers.from(Executors.newCachedThreadPool())

    fun database(): Scheduler {
        return DATABASE
    }

    fun api(): Scheduler {
        return API
    }

    fun download(): Scheduler {
        return DOWNLOAD
    }

    fun upload(): Scheduler {
        return UPLOAD
    }

    fun files(): Scheduler {
        return FILES
    }

    fun newThread(): Scheduler {
        return Schedulers.newThread()
    }

    fun worker(): Scheduler {
        return WORKER
    }

    fun computation(): Scheduler {
        return Schedulers.computation()
    }
}