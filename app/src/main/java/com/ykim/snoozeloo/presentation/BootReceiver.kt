package com.ykim.snoozeloo.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.presentation.util.doAsync
import com.ykim.snoozeloo.presentation.util.registerAlarm
import com.ykim.snoozeloo.presentation.util.toAlarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: AlarmRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        doAsync {
            repository.getAlarms().onEach { alarmList ->
                alarmList.forEach { alarmData ->
                    if (alarmData.enabled) {
                        context?.registerAlarm(alarmData.toAlarm(context))
                    }
                }
            }.collect()
        }
    }
}