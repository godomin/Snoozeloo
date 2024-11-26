package com.ykim.snoozeloo.domain

import java.util.Calendar

enum class DaysOfWeek(val mask: Int, val calendarDay: Int) {
    MONDAY(1 shl 0, Calendar.MONDAY),
    TUESDAY(1 shl 1, Calendar.TUESDAY),
    WEDNESDAY(1 shl 2, Calendar.WEDNESDAY),
    THURSDAY(1 shl 3, Calendar.THURSDAY),
    FRIDAY(1 shl 4, Calendar.FRIDAY),
    SATURDAY(1 shl 5, Calendar.SATURDAY),
    SUNDAY(1 shl 6, Calendar.SUNDAY)
}