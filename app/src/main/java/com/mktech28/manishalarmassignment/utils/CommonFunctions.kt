package com.mktech28.manishalarmassignment.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonFunctions {
    companion object {

        fun formatDateFromLong(dateInMillis: Long, dateFormat: String = "dd-MM-yyyy"): String {
            val date = Date(dateInMillis)
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            return formatter.format(date)
        }

        fun convertTimeTo12HourFormat(time24: String): String {
            val timeFormatter24 = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time24Date = timeFormatter24.parse(time24)

            val timeFormatter12 = SimpleDateFormat("hh:mm", Locale.getDefault())
            return timeFormatter12.format(time24Date)
        }

    }
}