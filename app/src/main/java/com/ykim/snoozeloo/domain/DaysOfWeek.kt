package com.ykim.snoozeloo.domain

enum class DaysOfWeek(val mask: Int) {
    MONDAY(1 shl 0),
    TUESDAY(1 shl 1),
    WEDNESDAY(1 shl 2),
    THURSDAY(1 shl 3),
    FRIDAY(1 shl 4),
    SATURDAY(1 shl 5),
    SUNDAY(1 shl 6)
}