package com.mktech28.manishalarmassignment.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mktech28.manishalarmassignment.alarmManager.AndroidAlarmScheduler
import com.mktech28.manishalarmassignment.data.entity.Alarm
import com.mktech28.manishalarmassignment.databinding.ActivityMainBinding
import com.mktech28.manishalarmassignment.recyclerview.AlarmRvAdapter
import com.mktech28.manishalarmassignment.recyclerview.RvItemClickListener
import com.mktech28.manishalarmassignment.utils.Constant.NOTIFICATION_PERMISSION_REQUEST_CODE
import com.mktech28.manishalarmassignment.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), RvItemClickListener {

    private lateinit var viewModel: AlarmViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AlarmRvAdapter

    private var selectedDate: Long? = null
    private var selectedTime: Pair<Int, Int>? = null
    private var alarm: Alarm? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askNotificationPermission()
        viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        init()

    }

    private fun init() {

        initActions()
        initObservers()
        initListeners()
    }

    private fun initActions() {
        binding.alarmRv.layoutManager = LinearLayoutManager(this)
        binding.alarmRv.setHasFixedSize(true)

        adapter = AlarmRvAdapter(this)
        binding.alarmRv.adapter = adapter
    }

    private fun initObservers() {
        viewModel.getAlarm().observe(this) { alarms ->
            if (alarms.isEmpty()) {
                Toast.makeText(this, "No Alarm Found!", Toast.LENGTH_SHORT).show()
            }
            adapter.setList(alarms as ArrayList<Alarm>)
        }
    }


    private fun initListeners() {
        binding.btnAddAlarm.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()

        datePicker.show(supportFragmentManager, "date_picker")

        datePicker.addOnPositiveButtonClickListener { selection ->

            selectedDate = selection
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val is24HourFormat = android.text.format.DateFormat.is24HourFormat(this)
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(if (is24HourFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time")
            .build()

        timePicker.show(supportFragmentManager, "time_picker")

        timePicker.addOnPositiveButtonClickListener {
            selectedTime = Pair(timePicker.hour, timePicker.minute)
            displaySelectedDateTime()
        }
    }

    private fun displaySelectedDateTime() {
        selectedDate?.let { date ->
            selectedTime?.let { time ->
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = date
                    set(Calendar.HOUR_OF_DAY, time.first)
                    set(Calendar.MINUTE, time.second)
                    set(Calendar.SECOND, 0)
                }
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val dateTime = dateFormatter.format(calendar.time)

                val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

                val currentCalendar = Calendar.getInstance()


                if (calendar.after(currentCalendar)) {
                    val alarm = Alarm(
                        alarmId = alarm?.alarmId ?: 0,
                        dateTime,
                        date = date,
                        hour = time.first,
                        minute = time.second,
                        active = true,
                        timeAmPm = amPm
                    )
                    viewModel.setAlarm(alarm) {
                        alarm.alarmId = it.toInt()
                        AndroidAlarmScheduler(application).scheduler(
                            alarm
                        )
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Selected date and time must be in the future",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    override fun stopAlarm(flag: Boolean, alarm: Alarm) {


        if(!flag){
            AndroidAlarmScheduler(context = this).cancel(
                alarm
            )
        }

        viewModel.updateAlarmStatus(flag, alarm.alarmId.toLong())
    }

    override fun deleteAlarm(alarm: Alarm) {
        try {
            viewModel.deleteAlarm(alarm)

            AndroidAlarmScheduler(context = this).cancel(
                alarm
            )
        } catch (ignore: Exception) {
        }
    }

    private fun askNotificationPermission() {

        var permissions = emptyArray<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            val permissionCheck = ContextCompat.checkSelfPermission(this, permissions[0])
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(
                this,
                permissions,
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // todo
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}