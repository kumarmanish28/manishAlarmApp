package com.mktech28.manishalarmassignment.alarmManager

import com.mktech28.manishalarmassignment.data.entity.Alarm

interface AlarmScheduler {
    fun scheduler(item: Alarm)
    fun cancel(item: Alarm)
}