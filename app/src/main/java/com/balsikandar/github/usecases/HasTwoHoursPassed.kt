package com.balsikandar.github.usecases

import java.util.concurrent.TimeUnit

class HasTwoHoursPassed {

    fun execute(lastCallTime: Long): Boolean {
        return TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - lastCallTime) > 2
    }
}