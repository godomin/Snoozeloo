package com.ykim.snoozeloo.presentation.detail

sealed interface DetailAction {
    data object OnClose : DetailAction
    data object OnSave : DetailAction
    data class OnNameChange(val name: String) : DetailAction
    data class OnHourChange(val hour: String) : DetailAction
    data class OnMinuteChange(val minute: String) : DetailAction
}