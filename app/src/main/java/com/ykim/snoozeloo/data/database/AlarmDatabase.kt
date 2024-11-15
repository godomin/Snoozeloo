package com.ykim.snoozeloo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlarmEntity::class], version = 1)
abstract class AlarmDatabase : RoomDatabase() {

    abstract val dao: AlarmDao

    companion object {
        const val DATABASE_NAME = "alarm_db"
    }
}