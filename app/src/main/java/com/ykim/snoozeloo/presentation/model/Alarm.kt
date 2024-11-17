package com.ykim.snoozeloo.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class Alarm(
    val name: String? = null,
    val time: String,
    val period: String,
    val timeLeft: String,
    val enabled: Boolean,
)