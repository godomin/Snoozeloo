package com.ykim.snoozeloo.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ykim.snoozeloo.Detail
import com.ykim.snoozeloo.domain.AlarmRepository
import com.ykim.snoozeloo.presentation.to12HourFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    alarmRepository: AlarmRepository
) : ViewModel() {

    var state by mutableStateOf(DetailState())
        private set

    init {
        viewModelScope.launch {
            val detail = savedStateHandle.toRoute<Detail>()
            detail.id?.let { id ->
                val alarm = alarmRepository.getAlarm(id)
                val (hour, minute) = alarm?.time?.to12HourFormat() ?: ("00" to "00")
                state = state.copy(
                    name = alarm?.name ?: "",
                    hour = hour,
                    minute = minute
                )
            }
        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            else -> Unit
        }
    }
}