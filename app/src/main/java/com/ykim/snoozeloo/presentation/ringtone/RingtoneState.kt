package com.ykim.snoozeloo.presentation.ringtone

import com.ykim.snoozeloo.presentation.model.Ringtone

data class RingtoneState(
    val ringtoneList: List<Ringtone> = emptyList(),
    val selectedUri: String = "",
)