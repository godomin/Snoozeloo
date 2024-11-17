package com.ykim.snoozeloo.presentation

import android.content.Context
import android.os.Build
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.data.database.AlarmEntity
import com.ykim.snoozeloo.presentation.model.Alarm
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

private const val FORMAT_12_HOUR = "hh:mm a"
private const val FORMAT_24_HOUR = "HH:mm"
private const val DAY_IN_MINUTES = 1440

fun AlarmEntity.toAlarm(context: Context): Alarm {
    val (time, period) = time.to12HourFormat()
    return Alarm(
        name = name,
        time = time,
        period = period,
        timeLeft = time.toMinutes(period).timeLeft(context),
        enabled = enabled,
    )
}

fun Int.to12HourFormat(): Pair<String, String> {
    val (time, period) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val time = LocalTime.of(this / 60, this % 60)
        time.format(DateTimeFormatter.ofPattern(FORMAT_12_HOUR)).split(" ")
    } else {
        val hour = this / 60
        val minute = this % 60
        val date = SimpleDateFormat(FORMAT_24_HOUR, Locale.getDefault()).parse("$hour:$minute")
        SimpleDateFormat(FORMAT_12_HOUR, Locale.getDefault()).format(date).split(" ")
    }
    return time to period
}

fun String.toMinutes(period: String): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val time = LocalTime.parse("$this $period", DateTimeFormatter.ofPattern(FORMAT_12_HOUR))
        time.hour * 60 + time.minute
    } else {
        val date = SimpleDateFormat(FORMAT_12_HOUR, Locale.getDefault()).parse("$this $period")
        val calendar = Calendar.getInstance().apply { time = date }
        calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }
}

fun Int.timeLeft(context: Context): String {
    val now = getCurrentTimeInMinutes()
    val timeLeft = if (this < now) {
        now - this
    } else {
        now - this + DAY_IN_MINUTES
    }
    val hour = timeLeft / 60
    val minute = timeLeft % 60
    return if (hour > 0) {
        context.getString(R.string.time_left_hour_min, hour, minute)
    } else {
        context.getString(R.string.time_left_min, minute)
    }
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