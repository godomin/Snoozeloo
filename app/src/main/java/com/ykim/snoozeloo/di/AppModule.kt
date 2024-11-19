package com.ykim.snoozeloo.di

import android.app.Application
import androidx.room.Room
import com.ykim.snoozeloo.data.AlarmRepositoryImpl
import com.ykim.snoozeloo.data.database.AlarmDao
import com.ykim.snoozeloo.data.database.AlarmDatabase
import com.ykim.snoozeloo.domain.AlarmRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindAlarmRepository(
        alarmRepositoryImpl: AlarmRepositoryImpl
    ): AlarmRepository

    companion object {
        @Provides
        @Singleton
        fun provideAlarmDatabase(app: Application): AlarmDatabase {
            return Room.databaseBuilder(
                app,
                AlarmDatabase::class.java,
                AlarmDatabase.DATABASE_NAME
            ).build()
        }

        @Provides
        @Singleton
        fun provideAlarmDao(database: AlarmDatabase): AlarmDao {
            return database.dao
        }
    }
}