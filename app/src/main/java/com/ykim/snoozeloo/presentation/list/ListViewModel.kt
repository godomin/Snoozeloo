package com.ykim.snoozeloo.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(

) : ViewModel() {

    var state by mutableStateOf(ListState())
        private set

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.OnAlarmToggleClick -> TODO()
            else -> Unit
        }
    }
}