package com.ykim.snoozeloo.presentation.trigger

sealed interface TriggerEvent {
    data object FinishScreen : TriggerEvent
}