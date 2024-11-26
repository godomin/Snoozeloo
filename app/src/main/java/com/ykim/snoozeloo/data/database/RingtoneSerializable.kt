package com.ykim.snoozeloo.data.database

import kotlinx.serialization.Serializable

@Serializable
sealed class RingtoneSerializable {
    @Serializable
    data class Ringtone(val uri: String) : RingtoneSerializable()

    @Serializable
    data object Silent : RingtoneSerializable()
}