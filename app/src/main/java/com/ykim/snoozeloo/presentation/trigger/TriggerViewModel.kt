package com.ykim.snoozeloo.presentation.trigger

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykim.snoozeloo.presentation.util.cancelAlarm
import com.ykim.snoozeloo.presentation.util.snoozeAlarm
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

    private var ringtone: Ringtone? = null
    private var ringtoneUri: String? = null
    private var vibrator: Vibrator? = null

    fun setInitialData(id: Int, time: String, name: String, volume: Int, vibrate: Boolean) {
        state = state.copy(
            id = id,
            time = time,
            name = name,
            volume = volume,
            vibrate = vibrate
        )
    }

    fun ringAlarm(uri: String) {
        if (state.vibrate) {
            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                context.getSystemService(VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 500, 1000, 500, 0),
                        0
                    )
                )
            } else {
                vibrator?.vibrate(500)
            }
        }
        ringtoneUri = uri
        ringtone = RingtoneManager.getRingtone(context, Uri.parse(uri)).apply {
            audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            volume = state.volume / 100f
            play()
        }
    }

    fun onAction(action: TriggerAction) {
        viewModelScope.launch {
            when (action) {
                is TriggerAction.OnTurnOff -> {
                    context.cancelAlarm(state.id ?: 0)
                    ringtone?.stop()
                    vibrator?.cancel()
                    eventChannel.send(TriggerEvent.FinishScreen)
                }

                is TriggerAction.OnSnooze -> {
                    context.cancelAlarm(state.id ?: 0)
                    ringtone?.stop()
                    vibrator?.cancel()
                    context.snoozeAlarm(
                        state.id ?: 0,
                        state.time,
                        state.name,
                        ringtoneUri,
                        state.volume,
                        state.vibrate
                    )
                    eventChannel.send(TriggerEvent.FinishScreen)
                }
            }
        }
    }
}