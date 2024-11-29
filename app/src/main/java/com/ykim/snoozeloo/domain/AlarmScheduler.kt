package com.ykim.snoozeloo.domain

import com.ykim.snoozeloo.domain.model.AlarmData

interface AlarmScheduler {
    fun scheduleAlarm(alarm: AlarmData)
    fun cancelAlarm(alarmId: Int)
    fun snoozeAlarm(alarm: AlarmData)
}