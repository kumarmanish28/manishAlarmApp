package com.mktech28.manishalarmassignment.alarmManager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mktech28.manishalarmassignment.alarmreceiver.AlarmReceiver
import com.mktech28.manishalarmassignment.data.MyAlarmDatabase
import com.mktech28.manishalarmassignment.data.entity.Alarm
import com.mktech28.manishalarmassignment.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AndroidAlarmScheduler(private val context: Context) : AlarmScheduler {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    override fun scheduler(item: Alarm) {
        item?.let {

            val calendar = Calendar.getInstance()

            // Set the date
            calendar.timeInMillis = it.date

            var adjustedHour = it.hour

            if (it.timeAmPm.equals("PM", ignoreCase = true) && it.hour < 12) {
                adjustedHour += 12
            } else if (it.timeAmPm.equals("AM", ignoreCase = true) && it.hour == 12) {
                adjustedHour = 0
            }

            // Set hour and minute
            calendar.set(Calendar.HOUR_OF_DAY, adjustedHour)
            calendar.set(Calendar.MINUTE, it.minute)
            calendar.set(Calendar.SECOND, 0)

            // Schedule the alarm using the calendar object
            val alarmTime = calendar.timeInMillis

            val pendingIntent = getPendingIntent(item);
            Log.d("CancelAlarm", "Cancelling alarm with PendingIntent:1111111111    $pendingIntent")


            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )

        }
    }

    override fun cancel(item: Alarm) {


        val pendingIntent = getPendingIntent(item)

        alarmManager.cancel(pendingIntent)

        // Broadcast an intent to stop the ringtone
        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constant.ACTION_STOP_ALARM_MANAGER
            putExtra(Constant.EXTRA_ALARM, item)
            putExtra(Constant.EXTRA_ID, item.alarmId)
        }
        context.sendBroadcast(stopIntent)


    }

    private fun getPendingIntent(alarm: Alarm): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constant.ACTION_ALARM_MANAGER
            putExtra(Constant.EXTRA_ALARM, alarm)
            putExtra(
                Constant.EXTRA_TIME,
                handleTime(alarm.hour.toInt(), alarm.minute.toInt()) + " ${alarm.timeAmPm}"
            )
        }
        return PendingIntent.getBroadcast(
            context,
            alarm.alarmId,  // Unique requestCode for each day
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun handleTime(hourOfDay: Int, minuteOfDay: Int): String {
        val hour12 = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
        val formattedHour = String.format("%02d", hour12)
        val formattedMinute = String.format("%02d", minuteOfDay)
        return "$formattedHour:$formattedMinute"
    }



    fun restartAllAlarms(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val activeAlarms = MyAlarmDatabase.getDatabace(context).alarmDao().getActiveAlarm()
            activeAlarms.let { alarms ->
                val alarmScheduler = AndroidAlarmScheduler(context)
                alarms.forEach { alarm ->
                    alarmScheduler.scheduler(alarm)
                }
            }
        }
    }
}