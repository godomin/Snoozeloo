package com.ykim.snoozeloo.presentation.list

import com.ykim.snoozeloo.presentation.model.Alarm

data class ListState(
    val alarmList: List<Alarm> = emptyList(),
)