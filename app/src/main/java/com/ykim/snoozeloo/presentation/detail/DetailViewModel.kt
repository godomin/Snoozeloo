package com.ykim.snoozeloo.presentation.detail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ykim.snoozeloo.Detail
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.presentation.timeLeft
import com.ykim.snoozeloo.presentation.to24HourFormat
import com.ykim.snoozeloo.presentation.toMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    var state by mutableStateOf(DetailState())
        private set

    init {
        viewModelScope.launch {
            val detail = savedStateHandle.toRoute<Detail>()
            detail.id?.let { id ->
                val alarm = alarmRepository.getAlarm(id)
                val (hour, minute) = alarm?.time?.to24HourFormat() ?: ("00" to "00")
                state = state.copy(
                    id = id,
                    name = alarm?.name ?: "",
                    hour = hour,
                    minute = minute,
                    enabled = alarm?.enabled ?: true
                )
                checkValidTime(hour, minute)
            }
        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.OnSave -> saveAlarm()
            is DetailAction.OnNameChange -> state = state.copy(name = action.name)
            is DetailAction.OnHourChange -> {
                checkValidTime(action.hour, state.minute)
                state = state.copy(hour = action.hour)
            }
            is DetailAction.OnMinuteChange -> {
                checkValidTime(state.hour, action.minute)
                state = state.copy(minute = action.minute)
            }
            else -> Unit
        }
    }

    private fun checkValidTime(hour: String, minute: String) {
        val valid = kotlin.runCatching {
            val timeLeft = "${hour}:${minute}".toMinutes().timeLeft(context)
            state = state.copy(timeLeft = timeLeft)
        }.isSuccess
        state = state.copy(isValidTime = valid)
    }

    private fun saveAlarm() {
        viewModelScope.launch {
            alarmRepository.updateAlarm(
                AlarmData(
                    id = state.id,
                    name = state.name,
                    time = "${state.hour}:${state.minute}".toMinutes(),
                    enabled = state.enabled
                )
            )
        }
    }
}