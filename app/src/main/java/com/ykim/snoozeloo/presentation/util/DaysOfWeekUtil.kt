package com.ykim.snoozeloo.presentation.util

import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.domain.DaysOfWeek
import kotlin.time.Duration.Companion.days

fun DaysOfWeek.getNameResourceId(): Int {
    return when (this) {
        DaysOfWeek.MONDAY -> R.string.monday
        DaysOfWeek.TUESDAY -> R.string.tuesday
        DaysOfWeek.WEDNESDAY -> R.string.wednesday
        DaysOfWeek.THURSDAY -> R.string.thursday
        DaysOfWeek.FRIDAY -> R.string.friday
        DaysOfWeek.SATURDAY -> R.string.saturday
        DaysOfWeek.SUNDAY -> R.string.sunday
    }
}

fun Int.weekdayEnabled(): Boolean {
    val weekdayMask = DaysOfWeek.MONDAY.mask or
            DaysOfWeek.TUESDAY.mask or
            DaysOfWeek.WEDNESDAY.mask or
            DaysOfWeek.THURSDAY.mask or
            DaysOfWeek.FRIDAY.mask
    return (this and weekdayMask) != 0
}

fun Int.selected(day: DaysOfWeek): Boolean {
    return (this and day.mask) != 0
}

fun Int.toggle(day: DaysOfWeek): Int {
    return if (selected(day)) {
        this and day.mask.inv()
    } else {
        this or day.mask
    }
}

fun Int.getEnabledDays(): List<Int> {
    val enabledDays = mutableListOf<Int>()
    for (day in DaysOfWeek.entries) {
        if (selected(day)) {
            enabledDays.add(day.calendarDay)
        }
    }
    if (enabledDays.isEmpty()) {
        DaysOfWeek.entries.forEach { enabledDays.add(it.calendarDay) }
    }
    return enabledDays
}