package com.ykim.snoozeloo.domain

import com.ykim.snoozeloo.domain.model.AlarmData
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun updateAlarm(alarmData: AlarmData)

    suspend fun deleteAlarm(alarmData: AlarmData)

    fun getAlarms(): Flow<List<AlarmData>>
}