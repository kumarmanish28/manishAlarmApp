package com.mktech28.manishalarmassignment.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mktech28.manishalarmassignment.R
import com.mktech28.manishalarmassignment.data.entity.Alarm
import com.mktech28.manishalarmassignment.utils.CommonFunctions


class AlarmRvAdapter(
    private val cardListener: RvItemClickListener,
    private var dataList: ArrayList<Alarm> = ArrayList()
) :
    RecyclerView.Adapter<AlarmRvAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.alarm_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(dataList: ArrayList<Alarm>) {
        this.dataList = dataList
        this.dataList.reverse()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val alarm = dataList[position]

        val date = CommonFunctions.formatDateFromLong(alarm.date)
        val time =
            "${if (alarm.hour < 10) "0" + alarm.hour else alarm.hour}:${if (alarm.minute < 10) "0" + alarm.minute else alarm.minute}"
        val timeApPm = alarm.timeAmPm
        val isActive = alarm.active

        holder.alarmDate.text = date
        holder.alramTime.text = CommonFunctions.convertTimeTo12HourFormat(time)
        holder.timeAmAndPm.text = timeApPm
        holder.switch.isChecked = isActive

        holder.switch.setOnClickListener {
            cardListener.stopAlarm(holder.switch.isChecked, alarm)
        }

        holder.ivDeleteAlarm.setOnClickListener {
            cardListener.deleteAlarm(alarm)
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.findViewById(R.id.CardTime_Card)
        var alramTime: TextView = itemView.findViewById(R.id.tv_alarm_time)
        var alarmDate: TextView = itemView.findViewById(R.id.tv_alarm_date)
        var timeAmAndPm: TextView = itemView.findViewById(R.id.tv_time_am_pm)
        var switch: SwitchCompat = itemView.findViewById(R.id.switch_compact)
        var ivDeleteAlarm: ImageView = itemView.findViewById(R.id.iv_delete_alarm)

    }

}