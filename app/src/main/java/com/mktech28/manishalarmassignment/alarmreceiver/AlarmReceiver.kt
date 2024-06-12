package com.mktech28.manishalarmassignment.alarmreceiver


import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.mktech28.manishalarmassignment.alarmManager.AndroidAlarmScheduler
import com.mktech28.manishalarmassignment.data.MyAlarmDatabase
import com.mktech28.manishalarmassignment.data.entity.Alarm
import com.mktech28.manishalarmassignment.data.repository.AlarmRepository
import com.mktech28.manishalarmassignment.services.RestartAlarmsService
import com.mktech28.manishalarmassignment.utils.Constant
import com.mktech28.manishalarmassignment.utils.Notification
import com.mktech28.manishalarmassignment.utils.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private var ringtone: Ringtone? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Constant.ACTION_ALARM_MANAGER)) {

            var alarm = getAlarmFromIntent(intent)
            val time = intent?.getStringExtra(Constant.EXTRA_TIME) ?: "Unknown time"
            Notification(
                NotificationId = alarm?.alarmId ?: 1, // Unique ID for the notification
                title = "Alarm Time Is : $time",
                description = "Alarm is trigger please wake up"
            ).displayNotification(context!!)

            if (ringtone == null || !ringtone!!.isPlaying) {
                val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ringtone = RingtoneManager.getRingtone(context, alarmUri)
            }

            ringtone?.play()

            SharedPreferenceUtils.setCurrentExecutedAlarm(context, alarm!!.alarmId)


        } else if (Intent.ACTION_BOOT_COMPLETED == intent?.action) {

            context?.startService(Intent(context, RestartAlarmsService::class.java))
            AndroidAlarmScheduler(context!!).restartAllAlarms(context)

        } else if (intent?.action.equals(Constant.ACTION_STOP_ALARM_MANAGER)) {
            val alarmId = intent?.getIntExtra(Constant.EXTRA_ID, -1)
            val runningAlarmId = SharedPreferenceUtils.getCurrentExecutedAlarm(context!!)
            if (alarmId != -1 && alarmId == runningAlarmId) {
                val alarm = getAlarmFromIntent(intent)
                removeNotification(context, alarmId)
                try {
                    ringtone?.stop()
                    CoroutineScope(Dispatchers.IO).launch {
                        val db = MyAlarmDatabase.getDatabace(context)
                        val alarmDao = db.alarmDao()
                        val repository = AlarmRepository(alarmDao)
                        repository.deleteAlarm(alarm!!)
                    }

                } catch (ignore: Exception) {
                }
            }
        }
    }

    private fun removeNotification(context: Context?, notificationId: Int?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId!!)
    }

    private fun getAlarmFromIntent(intent: Intent?): Alarm? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Constant.EXTRA_ALARM, Alarm::class.java)
        } else {
            intent?.getParcelableExtra(Constant.EXTRA_ALARM)
        }
    }

}

