package com.ykim.snoozeloo.presentation.navigation

import com.ykim.snoozeloo.presentation.model.Alarm
import kotlinx.serialization.Serializable

@Serializable
object ListScreen

@Serializable
data class DetailScreen(
    val alarm: Alarm?,
)

@Serializable
data class RingtoneScreen(
    val ringtoneUri: String
)