package com.ykim.snoozeloo.presentation.util

import android.content.Context
import android.os.Build
import com.ykim.snoozeloo.R
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val FORMAT_12_HOUR = "hh:mm a"
private const val FORMAT_24_HOUR = "HH:mm"
private const val DAY_IN_MINUTES = 1440
private const val FOUR_AM_IN_MINUTES = 240
private const val TEN_AM_IN_MINUTES = 600
private const val EIGHT_HOUR_IN_MINUTES = 480
private const val SNOOZE_MINUTES = 5

// 870 -> "02:30 PM"
fun Int.to12HourFormat(): Pair<String, String> {
    val (time, period) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val time = LocalTime.of(this / 60, this % 60)
        time.format(DateTimeFormatter.ofPattern(FORMAT_12_HOUR, Locale.ENGLISH)).split(" ")
    } else {
        val hour = this / 60
        val minute = this % 60
        val date = SimpleDateFormat(FORMAT_24_HOUR, Locale.getDefault()).parse("$hour:$minute")
        SimpleDateFormat(FORMAT_12_HOUR, Locale.ENGLISH).format(date).split(" ")
    }
    return time to period
}

// 870 -> "14", "30"
fun Int.to24HourFormat(): Pair<String, String> {
    return (this / 60).toTwoDigitString() to (this % 60).toTwoDigitString()
}

// "02:30 PM" -> "14", "30"
fun String.to24HourFormat(period: String): Pair<String, String> {
    return this.toMinutes(period).to24HourFormat()
}

// "02:30 PM" -> 870
fun String.toMinutes(period: String): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val time = LocalTime.parse(
            "$this $period",
            DateTimeFormatter.ofPattern(FORMAT_12_HOUR, Locale.ENGLISH)
        )
        time.hour * 60 + time.minute
    } else {
        val date = SimpleDateFormat(FORMAT_12_HOUR, Locale.ENGLISH).parse("$this $period")
        val calendar = Calendar.getInstance().apply { time = date }
        calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }
}

// "14:30" -> 870
fun String.toMinutes(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val time =
            LocalTime.parse(this, DateTimeFormatter.ofPattern(FORMAT_24_HOUR, Locale.ENGLISH))
        time.hour * 60 + time.minute
    } else {
        val date = SimpleDateFormat(FORMAT_12_HOUR, Locale.ENGLISH).parse(this)
        val calendar = Calendar.getInstance().apply { time = date }
        calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }
}

fun Int.timeLeft(context: Context, enabledDays: Int): String {
    val now = Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val closestDate = getClosestDate(this / 60, this % 60, enabledDays)
    val diffMillis = closestDate.timeInMillis - now.timeInMillis
    val day = TimeUnit.MILLISECONDS.toDays(diffMillis)
    val hour = TimeUnit.MILLISECONDS.toHours(diffMillis) % 24
    val minute = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60
    return if (day > 0) {
        context.getString(R.string.time_left_day_hour_min, day, hour, minute)
    } else if (hour > 0) {
        context.getString(R.string.time_left_hour_min, hour, minute)
    } else {
        context.getString(R.string.time_left_min, minute)
    }
}

fun Int.bedTimeLeft(context: Context, enabledDays: Int): String {
    return if (enabledDays.weekdayEnabled() && this in FOUR_AM_IN_MINUTES..TEN_AM_IN_MINUTES) {
        val now = getCurrentTimeInMinutes()
        val timeLeft = (this - now + DAY_IN_MINUTES) % DAY_IN_MINUTES
        if (timeLeft >= EIGHT_HOUR_IN_MINUTES) {
            val bedTime = (this - EIGHT_HOUR_IN_MINUTES + DAY_IN_MINUTES) % DAY_IN_MINUTES
            val bedTimeStr = bedTime.to12HourFormat().toList().joinToString("")
            context.getString(R.string.bed_time_left, bedTimeStr)
        } else {
            ""
        }
    } else {
        ""
    }
}

private fun Int.toTwoDigitString(): String {
    return String.format(Locale.ENGLISH, "%02d", this)
}

private fun getCurrentTimeInMinutes(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val time = LocalTime.now()
        time.hour * 60 + time.minute
    } else {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return hourOfDay * 60 + minute
    }
}

fun getClosestDate(
    hour: Int,
    minute: Int,
    enabledDays: Int
): Calendar {
    val now = Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val targetDays = enabledDays.getEnabledDays()
    val closestDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    var minDiff = Long.MAX_VALUE
    for (day in targetDays) {
        val targetDate = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (targetDate.before(now)) {
            targetDate.add(Calendar.WEEK_OF_YEAR, 1)
        }

        val diff = targetDate.timeInMillis - now.timeInMillis
        if (diff < minDiff) {
            minDiff = diff
            closestDate.timeInMillis = targetDate.timeInMillis
        }
    }
    return closestDate
}

fun getSnoozedTime(
    hour: Int,
    minute: Int,
): Calendar {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.MINUTE, SNOOZE_MINUTES)
    }
}