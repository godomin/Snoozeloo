package com.ykim.snoozeloo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String? = null,
    val time: Int,
    val enabled: Boolean,
) {
    companion object {
        operator fun invoke(
            id: Int? = null,
            name: String? = null,
            time: Int,
            enabled: Boolean
        ) = if (id == null) AlarmEntity(
            name = name,
            time = time,
            enabled = enabled
        ) else AlarmEntity(
            id = id,
            name = name,
            time = time,
            enabled = enabled
        )
    }
}
