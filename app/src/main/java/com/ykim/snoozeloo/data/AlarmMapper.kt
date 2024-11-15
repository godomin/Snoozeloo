package com.ykim.snoozeloo.data

import com.ykim.snoozeloo.data.database.AlarmEntity
import com.ykim.snoozeloo.domain.model.AlarmData

fun AlarmEntity.toAlarmData(): AlarmData {
    return AlarmData(
        name = name,
        time = time,
        enabled = enabled,
    )
}

fun AlarmData.toAlarmEntity(): AlarmEntity {
    return AlarmEntity(
        name = name,
        time = time,
        enabled = enabled,
    )
}