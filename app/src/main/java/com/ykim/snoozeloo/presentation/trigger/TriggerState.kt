package com.ykim.snoozeloo.presentation.trigger

data class TriggerState(
    val id: Int? = null,
    val time: String = "",
    val name: String = "",
    val volume: Int = 50,
    val vibrate: Boolean = false
)
