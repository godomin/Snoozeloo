package com.ykim.snoozeloo.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ykim.snoozeloo.domain.ALARM_ID
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.domain.AlarmScheduler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class AlarmRescheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repository: AlarmRepository

    @Inject
    lateinit var scheduler: AlarmScheduler

    override suspend fun doWork(): Result {
        inputData.getInt(ALARM_ID, -1).let { alarmId ->
            if (alarmId == -1) return Result.failure()
            repository.getAlarm(alarmId)?.let { alarm ->
                scheduler.scheduleAlarm(alarm)
            }
        }
        return Result.success()
    }
}