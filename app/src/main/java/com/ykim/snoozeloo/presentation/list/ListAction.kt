package com.ykim.snoozeloo.presentation.list

import com.ykim.snoozeloo.presentation.model.Alarm

sealed interface ListAction {
    data object OnAddAlarmClick : ListAction
    data class OnEditAlarmClick(val alarm: Alarm) : ListAction
    data class OnAlarmToggleClick(val alarm: Alarm) : ListAction
}