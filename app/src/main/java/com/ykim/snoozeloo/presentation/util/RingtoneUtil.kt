package com.ykim.snoozeloo.presentation.util

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri

const val KEY_RINGTONE_URI = "ringtoneUri"

fun String.getRingtoneTitle(context: Context): String {
    val uri = if (isValidUri(context)) Uri.parse(this) else getDefaultRingtoneUri()
    return RingtoneManager.getRingtone(context, uri).getTitle(context)
}

fun String.isValidUri(context: Context): Boolean {
    val uri = Uri.parse(this)
    return try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        val isValid = cursor != null && cursor.count > 0
        isValid
    } catch (e: Exception) {
        false
    }
}

fun getDefaultRingtoneUri(): Uri {
    return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
}