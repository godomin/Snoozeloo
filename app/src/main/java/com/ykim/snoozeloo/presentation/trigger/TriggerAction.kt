package com.ykim.snoozeloo.presentation.trigger

sealed interface TriggerAction {
    data object OnTurnOff : TriggerAction
    data object OnSnooze : TriggerAction
}