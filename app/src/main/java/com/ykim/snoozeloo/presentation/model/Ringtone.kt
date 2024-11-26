package com.ykim.snoozeloo.presentation.model

sealed class Ringtone {
    data class Normal(val title: String, val uri: String) : Ringtone()
    data object Silent : Ringtone()
}