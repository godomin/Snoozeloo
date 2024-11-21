package com.ykim.snoozeloo.presentation.util

import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.domain.DaysOfWeek

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