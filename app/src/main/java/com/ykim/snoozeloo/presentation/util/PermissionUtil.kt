package com.ykim.snoozeloo.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

fun ComponentActivity.shouldShowNotificationRationale(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)
}

private fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        hasPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        true
    }
}