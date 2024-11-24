package com.ykim.snoozeloo.presentation.ringtone

import com.ykim.snoozeloo.presentation.model.Ringtone

sealed interface RingtoneAction {
    data object OnBackPress : RingtoneAction
    data class OnItemClick(val ringtone: Ringtone) : RingtoneAction
}