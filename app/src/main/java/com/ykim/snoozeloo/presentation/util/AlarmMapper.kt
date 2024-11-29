package com.ykim.snoozeloo.presentation.util

import android.content.Context
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.domain.model.RingtoneData
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.model.Ringtone

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
        ringtone = ringtone.toRingtone(context),
        volume = volume,
        isVibrate = isVibrate
    )
}

fun Alarm.toAlarmData(): AlarmData {
    return AlarmData(
        id = id,
        name = name,
        time = time.toMinutes(period),
        enabled = enabled,
        enabledDays = enabledDays,
        ringtone = ringtone.toRingtoneData(),
        volume = volume,
        isVibrate = isVibrate,
    )
}

fun RingtoneData.toRingtone(context: Context): Ringtone {
    return when (this) {
        is RingtoneData.Ringtone -> Ringtone.Normal(uri.getRingtoneTitle(context), uri)
        is RingtoneData.Silent -> Ringtone.Silent
    }
}

fun Ringtone.toRingtoneData(): RingtoneData {
    return when (this) {
        is Ringtone.Normal -> RingtoneData.Ringtone(uri)
        is Ringtone.Silent -> RingtoneData.Silent
    }
}