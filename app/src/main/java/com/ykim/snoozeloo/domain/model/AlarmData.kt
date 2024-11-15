package com.ykim.snoozeloo.domain.model

data class AlarmData(
    val name: String? = null,
    val time: Long,
    val enabled: Boolean,
)
