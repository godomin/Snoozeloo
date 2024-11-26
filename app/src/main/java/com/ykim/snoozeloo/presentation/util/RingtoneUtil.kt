package com.ykim.snoozeloo.presentation.util

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import com.ykim.snoozeloo.R
import com.ykim.snoozeloo.presentation.model.Ringtone

const val KEY_RINGTONE_URI = "ringtone"

fun String.getRingtoneTitle(context: Context): String {
    if (isEmpty()) return context.getString(R.string.silent)
    val uri = if (isValidUri(context)) Uri.parse(this) else getDefaultRingtoneUri()
    return RingtoneManager.getRingtone(context, uri).getTitle(context)
}

fun String.isValidUri(context: Context): Boolean {
    val uri = Uri.parse(this)
    return try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        val isValid = cursor != null && cursor.count > 0
        cursor?.close()
        isValid
    } catch (e: Exception) {
        false
    }
}

private fun getDefaultRingtoneUri(): Uri {
    return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
}

fun getDefaultRingtone(context: Context): Ringtone {
    val defaultUri = getDefaultRingtoneUri().toString()
    return Ringtone.Normal(defaultUri.getRingtoneTitle(context), defaultUri)
}

fun Ringtone.getTitle(context: Context): String {
    return when (this) {
        is Ringtone.Normal -> title
        Ringtone.Silent -> context.getString(R.string.silent)
    }
}

fun Ringtone.getUri(): String {
    return when (this) {
        is Ringtone.Normal -> uri
        Ringtone.Silent -> ""
    }

}