package com.ykim.snoozeloo.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.domain.AlarmScheduler
import com.ykim.snoozeloo.data.util.doAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: AlarmRepository

    @Inject
    lateinit var scheduler: AlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            doAsync {
                repository.getAlarms().onEach { alarmList ->
                    alarmList.forEach { alarmData ->
                        if (alarmData.enabled) {
                            scheduler.scheduleAlarm(alarmData)
                        }
                    }
                }.collect()
            }
        }
    }
}