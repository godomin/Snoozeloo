package com.ykim.snoozeloo.presentation.list

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.toAlarm
import com.ykim.snoozeloo.presentation.toAlarmData
import com.ykim.snoozeloo.presentation.util.cancelAlarm
import com.ykim.snoozeloo.presentation.util.registerAlarm
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    var state by mutableStateOf(ListState())
        private set

    init {
        checkOverlayPermissionGranted()
        alarmRepository.getAlarms()
            .onEach { alarmDataList ->
                state = state.copy(
                    alarmList = alarmDataList.map { it.toAlarm(context) }
                )
                state = state.copy(isLoading = false)
            }.launchIn(viewModelScope)
    }

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.OnAlarmToggleClick -> onAlarmToggle(action.alarm)
            is ListAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(
                    shouldShowNotificationRationale = action.showRationale
                )
            }
            is ListAction.DismissRationaleDialog -> {
                state = state.copy(
                    shouldShowNotificationRationale = false
                )
            }
            else -> Unit
        }
    }

    private fun onAlarmToggle(alarm: Alarm) {
        val toggledAlarm = alarm.copy(enabled = !alarm.enabled)
        state = state.copy(
            alarmList = state.alarmList.map {
                if (it.id == alarm.id) {
                    toggledAlarm
                } else {
                    it
                }
            }
        )
        viewModelScope.launch {
            alarmRepository.updateAlarm(toggledAlarm.toAlarmData())
        }
        if (toggledAlarm.enabled) {
            context.registerAlarm(toggledAlarm)
        } else {
            context.cancelAlarm(alarm.id ?: 0)
        }
    }

    private fun checkOverlayPermissionGranted() {
        val granted = Settings.canDrawOverlays(context)
        state = state.copy(
            isOverlayPermissionGranted = granted
        )
    }
}