package com.mktech28.manishalarmassignment.utils

import android.content.Context

class SharedPreferenceUtils {

    companion object {
        private const val SHARED_PREFERENCE_FILE: String = "SHARED_PREFERENCE_FILE"

        private const val STAT_OF_ALARM = "STAT_OF_ALARM"


        fun getCurrentExecutedAlarm(mContext: Context): Int {
            val sharedPreferences =
                mContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(STAT_OF_ALARM, -1)
        }

        fun setCurrentExecutedAlarm(mContext: Context, alarmId: Int) {
            val sharedPreferences =
                mContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(STAT_OF_ALARM, alarmId)
            editor.apply()
        }

    }
}