package com.mktech28.manishalarmassignment.data.repository

import androidx.lifecycle.LiveData
import com.mktech28.manishalarmassignment.data.dao.AlarmDao
import com.mktech28.manishalarmassignment.data.entity.Alarm

class AlarmRepository(private val alarmDao: AlarmDao) {

    suspend fun setAlarm(alarm: Alarm) = alarmDao.setAlarm(alarm)

    suspend fun deleteAlarm(alarm: Alarm) = alarmDao.deleteAlarm(alarm)

    fun getAlarm(): LiveData<List<Alarm>> = alarmDao.getAlarm()
    fun getActiveAlarm(): List<Alarm> = alarmDao.getActiveAlarm()
    suspend fun updateAlarmStatus(flag: Boolean, alarmId: Long) =
        alarmDao.updateAlarmStatus(flag, alarmId)


}