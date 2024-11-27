package com.ykim.snoozeloo.presentation.list

import com.ykim.snoozeloo.presentation.model.Alarm

sealed interface ListAction {
    data object OnAddAlarmClick : ListAction
    data class OnEditAlarmClick(val alarm: Alarm) : ListAction
    data class OnAlarmToggleClick(val alarm: Alarm) : ListAction
    data class SubmitNotificationPermissionInfo(val showRationale: Boolean) : ListAction
    data object DismissRationaleDialog : ListAction
    data class OnDeleteAlarmClick(val alarm: Alarm) : ListAction
    data class OnItemDragged(val isExpanded: Boolean, val alarm: Alarm) : ListAction
}