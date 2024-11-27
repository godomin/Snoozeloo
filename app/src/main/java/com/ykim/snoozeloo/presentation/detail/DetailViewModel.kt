package com.ykim.snoozeloo.presentation.detail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ykim.snoozeloo.presentation.navigation.DetailScreen
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.presentation.model.Alarm
import com.ykim.snoozeloo.presentation.model.Ringtone
import com.ykim.snoozeloo.presentation.navigation.AlarmType
import com.ykim.snoozeloo.presentation.util.cancelAlarm
import com.ykim.snoozeloo.presentation.util.getDefaultRingtone
import com.ykim.snoozeloo.presentation.util.getRingtoneTitle
import com.ykim.snoozeloo.presentation.util.getTitle
import com.ykim.snoozeloo.presentation.util.getUri
import com.ykim.snoozeloo.presentation.util.registerAlarm
import com.ykim.snoozeloo.presentation.util.timeLeft
import com.ykim.snoozeloo.presentation.util.to24HourFormat
import com.ykim.snoozeloo.presentation.util.toAlarm
import com.ykim.snoozeloo.presentation.util.toMinutes
import com.ykim.snoozeloo.presentation.util.toRingtoneData
import com.ykim.snoozeloo.presentation.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    var state by mutableStateOf(DetailState())
        private set

    private val typeMap = mapOf(typeOf<Alarm?>() to AlarmType)

    init {
        viewModelScope.launch {
            val detailScreen = savedStateHandle.toRoute<DetailScreen>(typeMap)
            detailScreen.alarm?.let { alarm ->
                val (hour, minute) = alarm.time.to24HourFormat(alarm.period)
                val ringtone = alarm.ringtone
                state = state.copy(
                    id = alarm.id,
                    name = alarm.name ?: "",
                    hour = hour,
                    minute = minute,
                    enabled = alarm.enabled,
                    enabledDays = alarm.enabledDays,
                    ringtoneUri = ringtone.getUri(),
                    ringtoneTitle = ringtone.getTitle(context),
                    volume = alarm.volume,
                    isVibrate = alarm.isVibrate
                )
                checkValidTime(hour, minute)
            } ?: run {
                val ringtone = getDefaultRingtone(context)
                state = state.copy(
                    ringtoneUri = ringtone.getUri(),
                    ringtoneTitle = ringtone.getTitle(context)
                )
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

            is DetailAction.OnVolumeChange -> {
                state = state.copy(volume = action.volume)
            }

            is DetailAction.OnVibrateChange -> {
                state = state.copy(isVibrate = !state.isVibrate)
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
            ringtone = getRingtone().toRingtoneData(),
            volume = state.volume,
            isVibrate = state.isVibrate
        )
        viewModelScope.launch {
            alarmRepository.updateAlarm(newAlarm)
        }
        if (newAlarm.enabled) {
            newAlarm.id?.let { context.cancelAlarm(it) }
            context.registerAlarm(newAlarm.toAlarm(context))
        }
    }

    private fun getRingtone(): Ringtone {
        return when (state.ringtoneUri) {
            "" -> Ringtone.Silent
            else -> Ringtone.Normal(state.ringtoneTitle, state.ringtoneUri)
        }
    }
}