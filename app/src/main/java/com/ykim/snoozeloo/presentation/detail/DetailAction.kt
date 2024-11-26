package com.ykim.snoozeloo.presentation.detail

import com.ykim.snoozeloo.domain.DaysOfWeek

sealed interface DetailAction {
    data object OnClose : DetailAction
    data object OnSave : DetailAction
    data class OnNameChange(val name: String) : DetailAction
    data class OnHourChange(val hour: String) : DetailAction
    data class OnMinuteChange(val minute: String) : DetailAction
    data class OnDayChange(val day: DaysOfWeek) : DetailAction
    data class OnRingtoneClick(val ringtoneUri: String) : DetailAction
    data class OnRingtoneChange(val ringtoneUri: String) : DetailAction
    data class OnVolumeChange(val volume: Int) : DetailAction
    data object OnVibrateChange : DetailAction
}