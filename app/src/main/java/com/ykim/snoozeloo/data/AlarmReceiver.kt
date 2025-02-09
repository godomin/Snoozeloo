package com.ykim.snoozeloo.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ykim.snoozeloo.presentation.trigger.TriggerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val newIntent = Intent(context, TriggerActivity::class.java).apply {
            intent?.extras?.let { putExtras(it) }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context?.startActivity(newIntent)
    }
}