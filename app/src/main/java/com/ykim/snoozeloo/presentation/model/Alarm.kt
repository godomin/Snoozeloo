package com.ykim.snoozeloo.presentation.model

data class Alarm(
    val name: String? = null,
    val time: String,
    val period: String,
    val timeLeft: String,
    val bedTimeLeft: String,
    val enabled: Boolean,
    val enabledDays: Int = 0,
    val ringtone: Ringtone,
    val volume: Int,
    val isVibrate: Boolean,
    val id: Int? = null,
)