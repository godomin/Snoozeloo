package com.ykim.snoozeloo.presentation.list

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.toAlarm
import com.ykim.snoozeloo.presentation.toAlarmData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    var state by mutableStateOf(ListState())
        private set

    init {
        alarmRepository.getAlarms()
            .onEach { alarmDataList ->
                state = state.copy(
                    alarmList = alarmDataList.map { it.toAlarm(context) }
                )
            }.launchIn(viewModelScope)
    }

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.OnAlarmToggleClick -> onAlarmToggle(action.alarm)
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
    }
}