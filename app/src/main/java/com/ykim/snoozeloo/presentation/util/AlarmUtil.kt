package com.ykim.snoozeloo.presentation.util

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ykim.snoozeloo.presentation.AlarmReceiver
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.presentation.trigger.TriggerActivity
import com.ykim.snoozeloo.presentation.model.Alarm
import java.util.Calendar

const val CHANNEL_ID = "SnoozelooChannelId"
const val ALARM_ID = "alarmId"
const val ALARM_NAME = "alarmName"
const val ALARM_TIME = "alarmTime"
const val RINGTONE_URI = "ringtoneUri"
const val VOLUME = "volume"
const val VIBRATE = "vibrate"

fun Context.registerAlarm(alarm: Alarm) {
    val (hour, minute) = alarm.time.to24HourFormat(alarm.period)
    val closestTime = getClosestDate(hour.toInt(), minute.toInt(), alarm.enabledDays)
    register(
        this,
        alarm.id,
        alarm.time,
        alarm.name,
        alarm.ringtone.getUri(),
        alarm.volume,
        alarm.isVibrate,
        closestTime
    )
}

fun Context.snoozeAlarm(
    id: Int?,
    time: String,
    name: String?,
    ringtoneUri: String?,
    volume: Int?,
    vibrate: Boolean?
) {
    val (hour, minute) = time.split(":").map { it.toInt() }
    val snoozedTime = getSnoozedTime(hour, minute)
    register(this, id, time, name, ringtoneUri, volume, vibrate, snoozedTime)
}

private fun register(
    context: Context,
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

fun Context.cancelAlarm(alarmId: Int) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(alarmId)
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
        .setSmallIcon(R.drawable.alarm_icon)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
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
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        createNotificationChannel(channel)
    }
}