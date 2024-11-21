package com.ykim.snoozeloo.presentation.detail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ykim.snoozeloo.DetailScreen
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.presentation.util.getDefaultRingtoneUri
import com.ykim.snoozeloo.presentation.util.getRingtoneTitle
import com.ykim.snoozeloo.presentation.util.registerAlarm
import com.ykim.snoozeloo.presentation.util.timeLeft
import com.ykim.snoozeloo.presentation.util.to24HourFormat
import com.ykim.snoozeloo.presentation.util.toAlarm
import com.ykim.snoozeloo.presentation.util.toMinutes
import com.ykim.snoozeloo.presentation.util.toggle
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
            val detailScreen = savedStateHandle.toRoute<DetailScreen>()
            detailScreen.id?.let { id ->
                val alarm = alarmRepository.getAlarm(id)
                val (hour, minute) = alarm?.time?.to24HourFormat() ?: ("00" to "00")
                val ringtoneUri = alarm?.ringtoneUri ?: getDefaultRingtoneUri().toString()
                state = state.copy(
                    id = id,
                    name = alarm?.name ?: "",
                    hour = hour,
                    minute = minute,
                    enabled = alarm?.enabled ?: true,
                    enabledDays = alarm?.enableDays ?: 0,
                    ringtoneUri = ringtoneUri,
                    ringtoneTitle = ringtoneUri.getRingtoneTitle(context)
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

            is DetailAction.OnDayChange -> {
                state = state.copy(enabledDays = state.enabledDays.toggle(action.day))
            }

            is DetailAction.OnRingtoneChange -> {
                state = state.copy(
                    ringtoneUri = action.ringtoneUri,
                    ringtoneTitle = action.ringtoneUri.getRingtoneTitle(context)
                )
            }
            else -> Unit
        }
    }

    private fun checkValidTime(hour: String, minute: String) {
        val valid = kotlin.runCatching {
            val timeLeft = "${hour}:${minute}".toMinutes().timeLeft(context, state.enabledDays)
            state = state.copy(timeLeft = timeLeft)
        }.isSuccess
        state = state.copy(isValidTime = valid)
    }

    private fun saveAlarm() {
        val newAlarm = AlarmData(
            id = state.id,
            name = state.name,
            time = "${state.hour}:${state.minute}".toMinutes(),
            enabled = state.enabled,
            enabledDays = state.enabledDays,
            ringtoneUri = state.ringtoneUri,
        )
        viewModelScope.launch {
            alarmRepository.updateAlarm(newAlarm)
        }
        context.registerAlarm(newAlarm.toAlarm(context))
    }
}