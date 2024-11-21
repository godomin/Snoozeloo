package com.ykim.snoozeloo.domain.model

data class AlarmData(
    val id: Int? = null,
    val name: String? = null,
    val time: Int,
    val enabled: Boolean,
    val enabledDays: Int = 0,
    val ringtoneUri: String,
)
