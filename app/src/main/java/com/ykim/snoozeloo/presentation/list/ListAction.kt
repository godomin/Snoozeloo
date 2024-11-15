package com.ykim.snoozeloo.presentation.list

sealed interface ListAction {
    data object OnAddAlarmClick : ListAction
}