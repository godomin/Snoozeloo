package com.ykim.snoozeloo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String? = null,
    val time: Int,
    val enabled: Boolean,
)
