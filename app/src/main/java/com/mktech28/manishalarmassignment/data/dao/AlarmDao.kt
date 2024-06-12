package com.mktech28.manishalarmassignment.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mktech28.manishalarmassignment.data.entity.Alarm


@Dao
interface AlarmDao {
    @Insert
    suspend fun setAlarm(alarm: Alarm): Long

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("select * from alarm_table")
    fun getAlarm(): LiveData<List<Alarm>>

    @Query("select * from alarm_table where active= 1")
    fun getActiveAlarm(): List<Alarm>

    @Query("update alarm_table set active =:flag where alarmId =:alarmId")
    fun updateAlarmStatus(flag: Boolean, alarmId: Long)

}