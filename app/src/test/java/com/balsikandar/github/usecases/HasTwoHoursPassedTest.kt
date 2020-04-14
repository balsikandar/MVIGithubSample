package com.balsikandar.github.usecases

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HasTwoHoursPassedTest {

    lateinit var hasTwoHoursPassed: HasTwoHoursPassed

    @Before
    fun setup() {
        hasTwoHoursPassed = HasTwoHoursPassed()
    }

    @Test
    fun timePassedLessThanTwoHours() {
        val timeWithinTwoHours = System.currentTimeMillis() - 7199999
        val status = hasTwoHoursPassed.execute(timeWithinTwoHours)

        assertThat(status).isFalse()
    }

    @Test
    fun timePassedMoreThanTwoHours() {
        val timeBeforeTwoHours = System.currentTimeMillis() - 72000000
        val status = hasTwoHoursPassed.execute(timeBeforeTwoHours)

        assertThat(status).isTrue()
    }
}