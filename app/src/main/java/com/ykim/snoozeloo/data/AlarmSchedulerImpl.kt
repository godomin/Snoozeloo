package com.ykim.snoozeloo.data

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ykim.snoozeloo.domain.ALARM_ID
import com.ykim.snoozeloo.domain.ALARM_NAME
import com.ykim.snoozeloo.domain.ALARM_TIME
import com.ykim.snoozeloo.domain.AlarmScheduler
import com.ykim.snoozeloo.domain.RINGTONE_URI
import com.ykim.snoozeloo.domain.VIBRATE
import com.ykim.snoozeloo.domain.VOLUME
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.presentation.util.getClosestDate
import com.ykim.snoozeloo.presentation.util.getSnoozedTime
import com.ykim.snoozeloo.presentation.util.to24HourFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AlarmScheduler {

    override fun scheduleAlarm(alarm: AlarmData) {
        val (hour, minute) = alarm.time.to24HourFormat()
        val closestTime = getClosestDate(hour.toInt(), minute.toInt(), alarm.enabledDays)
        schedule(
            alarm.id,
            "$hour:$minute",
            alarm.name,
            alarm.ringtone.getUri(),
            alarm.volume,
            alarm.isVibrate,
            closestTime
        )
    }

    override fun cancelAlarm(alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun snoozeAlarm(alarm: AlarmData) {
        val (hour, minute) = alarm.time.to24HourFormat()
        val snoozedTime = getSnoozedTime(hour.toInt(), minute.toInt())
        schedule(
            alarm.id,
            "$hour:$minute",
            alarm.name,
            alarm.ringtone.getUri(),
            alarm.volume,
            alarm.isVibrate,
            snoozedTime
        )
    }

    private fun schedule(
        id: Int?,
        time: String,
        name: String?,
        ringtoneUri: String?,
        volume: Int?,
        vibrate: Boolean?,
        target: Calendar
    ) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ALARM_ID, id)
            putExtra(ALARM_NAME, name)
            putExtra(ALARM_TIME, time)
            putExtra(RINGTONE_URI, ringtoneUri)
            putExtra(VOLUME, volume)
            putExtra(VIBRATE, vibrate)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            target.timeInMillis,
            pendingIntent
        )
    }
}