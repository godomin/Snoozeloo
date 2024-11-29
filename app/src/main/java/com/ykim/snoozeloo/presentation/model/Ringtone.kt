package com.ykim.snoozeloo.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Ringtone {
    @Serializable
    data class Normal(val title: String, val uri: String) : Ringtone()

    @Serializable
    data object Silent : Ringtone()
}