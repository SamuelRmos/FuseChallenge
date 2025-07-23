package com.samuelrmos.fusechallenge.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

private const val TIME_PATTERN = "HH:mm"

private const val DATE_PATTERN = "dd.MM HH:mm"

private const val DAY_OF_WEEK_PATTERN = "EEE HH:mm"

private const val TODAY = "Hoje,"

private const val TOMORROW = "Amanhã,"

fun String.formatDateTime(now: ZonedDateTime = now()): String {
    // Parse input date in UTC
    val inputDateTime = Instant.parse(this)
        .atZone(ZoneId.systemDefault())

    val formatterTime = DateTimeFormatter.ofPattern(TIME_PATTERN)
    val formatterDate = DateTimeFormatter.ofPattern(DATE_PATTERN)
    val formatterDayOfWeek = DateTimeFormatter.ofPattern(
        DAY_OF_WEEK_PATTERN,
        Locale("pt", "BR")
    )

    return when {
        inputDateTime.toLocalDate().isEqual(now.toLocalDate()) -> {
            "$TODAY ${inputDateTime.format(formatterTime)}"
        }

        inputDateTime.toLocalDate().isEqual(now.plusDays(1).toLocalDate()) -> {
            "$TOMORROW ${inputDateTime.format(formatterTime)}"
        }

        isSameWeek(now, inputDateTime) -> {
            formatDateSameWeek(inputDateTime.format(formatterDayOfWeek))
        }

        else -> {
            inputDateTime.format(formatterDate)
        }
    }
}

private fun formatDateSameWeek(date: String): String {
    val dayCapitalize = date.substring(0, 4).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale("pt")) else it.toString()
    }.replace(".", ",")

    return "$dayCapitalize ${date.substring(5, 10)}"
}

// Helper to check if two dates are in the same week (ISO week)
private fun isSameWeek(currentDate: ZonedDateTime, receivedDate: ZonedDateTime): Boolean {
    val weekFields = WeekFields.of(Locale("pt", "BR"))

    val weekCurrentDate = currentDate.get(weekFields.weekOfWeekBasedYear())
    val weekReceivedDate = receivedDate.get(weekFields.weekOfWeekBasedYear())
    val yearCurrentDate = currentDate.get(weekFields.weekBasedYear())
    val yearReceivedDate = receivedDate.get(weekFields.weekBasedYear())

    return weekCurrentDate == weekReceivedDate && yearCurrentDate == yearReceivedDate
}