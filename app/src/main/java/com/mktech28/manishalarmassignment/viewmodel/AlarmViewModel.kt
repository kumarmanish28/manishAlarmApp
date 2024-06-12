package com.mktech28.manishalarmassignment.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mktech28.manishalarmassignment.alarmManager.AndroidAlarmScheduler
import com.mktech28.manishalarmassignment.data.MyAlarmDatabase
import com.mktech28.manishalarmassignment.data.entity.Alarm
import com.mktech28.manishalarmassignment.data.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmRepository: AlarmRepository =
        AlarmRepository(alarmDao = MyAlarmDatabase.getDatabace(application).alarmDao())


    fun setAlarm(alarm: Alarm, callBack: (Long) -> Unit) {

        viewModelScope.launch {
            val result = alarmRepository.setAlarm(alarm)
            callBack(result)
        }

    }

    fun deleteAlarm(alarm: Alarm) {

        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.deleteAlarm(alarm)
        }
    }


    fun getAlarm(): LiveData<List<Alarm>> = alarmRepository.getAlarm()

    fun getActiveAlarm(): List<Alarm> = alarmRepository.getActiveAlarm()

    fun updateAlarmStatus(flag: Boolean, alarmId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.updateAlarmStatus(flag, alarmId)
        }
    }

}