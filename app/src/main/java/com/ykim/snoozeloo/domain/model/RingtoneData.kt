package com.ykim.snoozeloo.domain.model

sealed class RingtoneData {
    data class Ringtone(val uri: String) : RingtoneData()
    data object Silent : RingtoneData()
}