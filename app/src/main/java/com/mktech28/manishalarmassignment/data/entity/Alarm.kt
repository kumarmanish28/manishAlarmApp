package com.mktech28.manishalarmassignment.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var alarmId: Int,
    var alarmDateTime: String,
    var date: Long,
    var hour: Int,
    var minute: Int,
    var active: Boolean = true,
    var timeAmPm: String
): Serializable

