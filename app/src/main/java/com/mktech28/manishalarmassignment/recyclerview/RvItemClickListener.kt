package com.mktech28.manishalarmassignment.recyclerview

import com.mktech28.manishalarmassignment.data.entity.Alarm

interface RvItemClickListener {

    fun stopAlarm(flag: Boolean, alarm: Alarm)
    fun deleteAlarm(alarm: Alarm)
}