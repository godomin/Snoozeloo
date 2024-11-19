package com.ykim.snoozeloo.presentation.trigger

import android.content.Context
import android.media.RingtoneManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykim.snoozeloo.presentation.util.cancelAlarm
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TriggerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    var state by mutableStateOf(TriggerState())
        private set

    private val eventChannel = Channel<TriggerEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone.play()
    }

    fun setInitialData(id: Int, time: String, name: String) {
        state = state.copy(
            id = id,
            time = time,
            name = name
        )
    }

    fun onAction(action: TriggerAction) {
        viewModelScope.launch {
            when (action) {
                is TriggerAction.OnTurnOff -> {
                    eventChannel.send(TriggerEvent.TurnOffAlarm)
                    context.cancelAlarm(state.id ?: 0)
                }
            }
        }
    }
}