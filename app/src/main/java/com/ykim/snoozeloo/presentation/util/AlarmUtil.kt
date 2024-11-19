package com.ykim.snoozeloo.presentation.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ykim.snoozeloo.AlarmReceiver
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.presentation.TriggerActivity
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.to24HourFormat
import java.util.Calendar

const val CHANNEL_ID = "SnoozelooChannelId"
const val ALARM_ID = "alarmId"
const val ALARM_NAME = "alarmName"
const val ALARM_TIME = "alarmTime"

fun Context.registerAlarm(alarm: Alarm) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val (hour, minute) = alarm.time.to24HourFormat(alarm.period)
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour.toInt())
        set(Calendar.MINUTE, minute.toInt())
        set(Calendar.SECOND, 0)
        if (before(Calendar.getInstance())) {
            add(Calendar.DATE, 1)
        }
    }

    val intent = Intent(this, AlarmReceiver::class.java).apply {
        putExtra(ALARM_ID, alarm.id)
        putExtra(ALARM_NAME, alarm.name)
        putExtra(ALARM_TIME, alarm.time)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        alarm.id ?: 0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

fun Context.cancelAlarm(alarmId: Int) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        alarmId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
}

fun Context.showNotificationWithFullScreenIntent(receivedIntent: Intent?) {
    val alarmId = receivedIntent?.getIntExtra(ALARM_ID, 0) ?: 0
    val intent = Intent(this, TriggerActivity::class.java).apply {
        receivedIntent?.extras?.let { putExtras(it) }
    }
    val fullScreenIntent = PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(getString(R.string.app_name))
        .setContentText("content text")
        .setSmallIcon(R.drawable.alarm)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setFullScreenIntent(fullScreenIntent, true)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.buildChannel(this)
    notificationManager.notify(alarmId, builder.build())
}

private fun NotificationManager.buildChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = "description"
        }
        createNotificationChannel(channel)
    }
}