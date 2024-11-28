package com.ykim.snoozeloo.presentation.trigger

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioAttributes
import android.media.MediaPlayer
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
import com.ykim.snoozeloo.domain.AlarmScheduler
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.domain.model.RingtoneData
import com.ykim.snoozeloo.presentation.util.toMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TriggerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    var state by mutableStateOf(TriggerState())
        private set

    private val eventChannel = Channel<TriggerEvent>()
    val events = eventChannel.receiveAsFlow()

    private var ringtone: Ringtone? = null
    private var ringtoneUri: String? = null
    private var mediaPlayer: MediaPlayer? = null
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
            playVibration()
        }
        ringtoneUri = uri
        playRingtone()
    }

    fun onAction(action: TriggerAction) {
        viewModelScope.launch {
            when (action) {
                is TriggerAction.OnTurnOff -> {
                    stopAlarm()
                    eventChannel.send(TriggerEvent.FinishScreen)
                }

                is TriggerAction.OnSnooze -> {
                    stopAlarm()
                    snoozeAlarm()
                    eventChannel.send(TriggerEvent.FinishScreen)
                }
            }
        }
    }

    private fun stopAlarm() {
        state.id?.let { alarmScheduler.cancelAlarm(it) }
        ringtone?.stop()
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
        vibrator?.cancel()
    }

    private fun snoozeAlarm() {
        alarmScheduler.snoozeAlarm(AlarmData(
            id = state.id,
            time = state.time.toMinutes(),
            name = state.name,
            ringtone = RingtoneData.Ringtone(ringtoneUri!!),
            volume = state.volume,
            isVibrate = state.vibrate,
            enabled = true
        ))
    }

    private fun playVibration() {
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

    private fun playRingtone() {
        val alarmAudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone = RingtoneManager.getRingtone(context, Uri.parse(ringtoneUri)).apply {
                audioAttributes = alarmAudioAttributes
                volume = state.volume / 100f
                play()
            }
        } else {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.parse(ringtoneUri))
                setVolume(state.volume / 100f, state.volume / 100f)
                isLooping = true
                setAudioAttributes(alarmAudioAttributes)
                prepare()
                start()
            }
        }
    }
}