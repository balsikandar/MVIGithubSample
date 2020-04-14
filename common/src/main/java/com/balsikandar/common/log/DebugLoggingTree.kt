package com.balsikandar.common.log

import timber.log.Timber

class DebugLoggingTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "log.$tag", message, t)
    }
}