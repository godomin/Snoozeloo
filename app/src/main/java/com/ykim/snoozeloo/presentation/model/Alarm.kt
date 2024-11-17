package com.ykim.snoozeloo.presentation.model

data class Alarm(
    val id: Int,
    val name: String? = null,
    val time: String,
    val period: String,
    val timeLeft: String,
    val enabled: Boolean,
)