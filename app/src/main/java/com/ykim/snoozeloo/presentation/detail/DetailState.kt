package com.ykim.snoozeloo.presentation.detail

data class DetailState(
    val id: Int? = null,
    val hour: String = "",
    val minute: String = "",
    val name: String = "",
    val enabled: Boolean = true,
    val enabledDays: Int = 0,
    val timeLeft: String = "",
    val ringtoneTitle: String = "",
    val ringtoneUri: String = "",
    val volume: Int = 50,
    val isVibrate: Boolean = false,
    val isValidTime: Boolean = false,
)