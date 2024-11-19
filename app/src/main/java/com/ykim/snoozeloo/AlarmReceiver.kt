package com.ykim.snoozeloo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ykim.snoozeloo.presentation.TriggerActivity
import com.ykim.snoozeloo.presentation.util.showNotificationWithFullScreenIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //context?.showNotificationWithFullScreenIntent(intent)
        val newIntent = Intent(context, TriggerActivity::class.java).apply {
            intent?.extras?.let { putExtras(it) }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context?.startActivity(newIntent)
    }
}