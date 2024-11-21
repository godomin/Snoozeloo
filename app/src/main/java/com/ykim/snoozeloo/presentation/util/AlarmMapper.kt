package com.ykim.snoozeloo.presentation.util

import android.content.Context
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.presentation.model.Alarm

fun AlarmData.toAlarm(context: Context): Alarm {
    val (time, period) = time.to12HourFormat()
    return Alarm(
        id = id,
        name = name,
        time = time,
        period = period,
        timeLeft = time.toMinutes(period).timeLeft(context, enabledDays),
        bedTimeLeft = time.toMinutes(period).bedTimeLeft(context, enabledDays),
        enabled = enabled,
        enabledDays = enabledDays,
    )
}

fun Alarm.toAlarmData(): AlarmData {
    return AlarmData(
        id = id,
        name = name,
        time = time.toMinutes(period),
        enabled = enabled,
        enabledDays = enabledDays,
    )
}