package com.mktech28.manishalarmassignment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mktech28.manishalarmassignment.data.dao.AlarmDao
import com.mktech28.manishalarmassignment.data.entity.Alarm
import kotlin.concurrent.Volatile

@Database(entities = [Alarm::class], version = 1)
abstract class MyAlarmDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var instance: MyAlarmDatabase? = null

        fun getDatabace(context: Context): MyAlarmDatabase {
            if (instance != null) return instance!!
            synchronized(this) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyAlarmDatabase::class.java,
                    "AlarmDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                return instance!!
            }


        }


    }
}