package com.ykim.snoozeloo.presentation.trigger

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
    private val viewModel: TriggerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideNavigationBar()
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

    override fun onPause() {
        super.onPause()
        viewModel.handleAlarmFromActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }

    private fun hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }
}