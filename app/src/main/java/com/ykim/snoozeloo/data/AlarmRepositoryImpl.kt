package com.ykim.snoozeloo.data

import com.ykim.snoozeloo.data.database.AlarmDao
import com.ykim.snoozeloo.data.database.AlarmEntity
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.domain.model.AlarmData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmRepository {
    override suspend fun updateAlarm(alarmData: AlarmData) {
        alarmDao.upsertAlarm(alarmData.toAlarmEntity())
    }

    override suspend fun deleteAlarm(alarmData: AlarmData) {
        alarmDao.deleteAlarm(alarmData.toAlarmEntity())
    }

    override suspend fun getAlarm(id: Int): AlarmEntity? {
        return alarmDao.getAlarmById(id)
    }

    override fun getAlarms(): Flow<List<AlarmData>> {
        return alarmDao.getAlarms().map { entityList ->
            entityList.map { it.toAlarmData() }
        }
    }
}