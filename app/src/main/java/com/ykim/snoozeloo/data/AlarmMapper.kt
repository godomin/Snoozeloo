package com.ykim.snoozeloo.data

import com.ykim.snoozeloo.data.database.AlarmEntity
import com.ykim.snoozeloo.domain.model.AlarmData

fun AlarmEntity.toAlarmData(): AlarmData {
    return AlarmData(
        id = id,
        name = name,
        time = time,
        enabled = enabled,
    )
}

fun AlarmData.toAlarmEntity(): AlarmEntity {
    return AlarmEntity(
        id = id,
        name = name,
        time = time,
        enabled = enabled,
    )
}