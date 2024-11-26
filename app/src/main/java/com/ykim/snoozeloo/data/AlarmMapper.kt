package com.ykim.snoozeloo.data

import com.ykim.snoozeloo.data.database.AlarmEntity
import com.ykim.snoozeloo.data.database.RingtoneSerializable
import com.ykim.snoozeloo.domain.model.AlarmData
import com.ykim.snoozeloo.domain.model.RingtoneData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun AlarmEntity.toAlarmData(): AlarmData {
    return AlarmData(
        id = id,
        name = name,
        time = time,
        enabled = enabled,
        enabledDays = enabledDays,
        ringtone = ringtoneJson.toRingtoneSerializable().toRingtoneData(),
        volume = volume,
        isVibrate = isVibrate,
    )
}

fun AlarmData.toAlarmEntity(): AlarmEntity {
    return AlarmEntity(
        id = id,
        name = name,
        time = time,
        enabled = enabled,
        enabledDays = enabledDays,
        ringtoneJson = ringtone.toRingtoneSerializable().toJson(),
        volume = volume,
        isVibrate = isVibrate
    )
}

private fun RingtoneData.toRingtoneSerializable(): RingtoneSerializable {
    return when (this) {
        is RingtoneData.Ringtone -> RingtoneSerializable.Ringtone(uri)
        is RingtoneData.Silent -> RingtoneSerializable.Silent
    }
}

private fun RingtoneSerializable.toRingtoneData(): RingtoneData {
    return when (this) {
        is RingtoneSerializable.Ringtone -> RingtoneData.Ringtone(uri)
        is RingtoneSerializable.Silent -> RingtoneData.Silent
    }
}

private fun RingtoneSerializable.toJson(): String {
    return Json.encodeToString(this)
}

private fun String.toRingtoneSerializable(): RingtoneSerializable {
    return Json.decodeFromString<RingtoneSerializable>(this)
}
