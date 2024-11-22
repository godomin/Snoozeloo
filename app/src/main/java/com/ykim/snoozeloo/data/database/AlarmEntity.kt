package com.ykim.snoozeloo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String? = null,
    val time: Int,
    val enabled: Boolean,
    val enabledDays: Int,
    val ringtoneJson: String,
    val volume: Int,
    val isVibrate: Boolean
) {
    companion object {
        operator fun invoke(
            id: Int? = null,
            name: String? = null,
            time: Int,
            enabled: Boolean,
            enabledDays: Int = 0,
            ringtoneJson: String,
            volume: Int,
            isVibrate: Boolean
        ) = if (id == null) AlarmEntity(
            name = name,
            time = time,
            enabled = enabled,
            enabledDays = enabledDays,
            ringtoneJson = ringtoneJson,
            volume = volume,
            isVibrate = isVibrate
        ) else AlarmEntity(
            id = id,
            name = name,
            time = time,
            enabled = enabled,
            enabledDays = enabledDays,
            ringtoneJson = ringtoneJson,
            volume = volume,
            isVibrate = isVibrate
        )
    }
}
