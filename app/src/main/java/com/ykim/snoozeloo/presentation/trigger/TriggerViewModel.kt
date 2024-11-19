package com.ykim.snoozeloo.presentation.trigger

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TriggerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state by mutableStateOf(TriggerState())
        private set

    init {
        viewModelScope.launch {

        }
    }

    fun onAction(action: TriggerAction) {

    }
}