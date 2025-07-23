package com.samuelrmos.fusechallenge.util

import com.samuelrmos.fusechallenge.utils.formatDateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class DateExtensionTest {

    private lateinit var fixedNow: ZonedDateTime

    @Before
    fun setUp() {
        fixedNow = ZonedDateTime.of(
            2025, 7, 19, 10, 0, 0, 0,
            ZoneId.systemDefault()
        )
    }

    @Test
    fun `formatDateTime should returns today`() {
        val inputDate = "2025-07-19T15:30:00Z"

        assertEquals("Hoje, 12:30", inputDate.formatDateTime(fixedNow))
    }

    @Test
    fun `formatDateTime should returns tomorrow`() {
        val inputDate = "2025-07-20T15:30:00Z"

        assertEquals("Amanhã, 12:30", inputDate.formatDateTime(fixedNow))
    }

    @Test
    fun `formatDateTime should returns same week day`() {
        fixedNow = ZonedDateTime.of(
            2025, 7, 22, 10, 0, 0, 0,
            ZoneId.systemDefault()
        )
        val inputDate = "2025-07-24T15:30:00Z"

        assertEquals("Qui, 12:30", inputDate.formatDateTime(fixedNow))
    }

    @Test
    fun `formatDateTime should returns date and time`() {
        val inputDate = "2025-07-24T15:30:00Z"

        assertEquals("24.07 12:30", inputDate.formatDateTime(fixedNow))
    }
}