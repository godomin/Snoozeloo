package com.ykim.snoozeloo.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.ykim.snoozeloo.presentation.model.Alarm
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val AlarmType = object : NavType<Alarm?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Alarm? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Alarm? {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Alarm?): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: Alarm?) {
        bundle.putString(key, Json.encodeToString(value))
    }
}