package com.ykim.snoozeloo.presentation.trigger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ykim.snoozeloo.domain.ALARM_ID
import com.ykim.snoozeloo.domain.ALARM_NAME
import com.ykim.snoozeloo.domain.ALARM_TIME
import com.ykim.snoozeloo.domain.RINGTONE_URI
import com.ykim.snoozeloo.domain.VIBRATE
import com.ykim.snoozeloo.domain.VOLUME
import com.ykim.snoozeloo.presentation.util.getDefaultRingtone
import com.ykim.snoozeloo.presentation.util.getUri
import com.ykim.snoozeloo.presentation.util.turnScreenOffAndKeyguardOn
import com.ykim.snoozeloo.presentation.util.turnScreenOnAndKeyguardOff
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TriggerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val alarmId = intent.getIntExtra(ALARM_ID, 0)
        val alarmTime = intent.getStringExtra(ALARM_TIME) ?: ""
        val alarmName = intent.getStringExtra(ALARM_NAME) ?: ""
        val ringtoneUri = intent.getStringExtra(RINGTONE_URI) ?: getDefaultRingtone(this).getUri()
        val volume = intent.getIntExtra(VOLUME, 50)
        val vibrate = intent.getBooleanExtra(VIBRATE, false)
        setContent {
            SnoozelooTheme {
                TriggerScreenRoot(
                    alarmId = alarmId,
                    alarmTime = alarmTime,
                    alarmName = alarmName,
                    ringtoneUri = ringtoneUri,
                    volume = volume,
                    vibrate = vibrate
                )
            }
        }
        turnScreenOnAndKeyguardOff()
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}