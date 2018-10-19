package com.xinwang.xinwallet.tools.util

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.squareup.timessquare.CalendarPickerView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_date_picker.*
import java.text.DateFormat
import java.util.*


class DatePickerActivity : XinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)
        titleSetting()
        val maxDate = Calendar.getInstance()
        val selectDay = Calendar.getInstance()
        maxDate.add(Calendar.DATE, +1)
        selectDay.add(Calendar.DATE, 0)
        val lastYear = Calendar.getInstance()
        lastYear.add(Calendar.YEAR, -1)
        datePicker.init(lastYear.time, maxDate.time)
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(selectDay.time)
        datePicker.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                val dates = datePicker.selectedDates
                val firstDate = Calendar.getInstance()
                val lastDate = Calendar.getInstance()
                firstDate.time = dates[0]
                lastDate.time = dates[dates.size - 1]
                val t1 = DateFormat.getDateInstance(DateFormat.SHORT).format(dates[0])
                val t2 = DateFormat.getDateInstance(DateFormat.SHORT).format(dates[dates.size - 1])
                tv.text = t1 + "-" + t2

            }

            override fun onDateUnselected(date: Date) {

            }

        })
    }

    fun buttonOnclick(view: View) {
        val dates = datePicker.selectedDates

        val intent = Intent().putExtra("date1", dates[0].time.toString())
        intent.putExtra("date2", dates[dates.size - 1].time.toString())
        setResult(20, intent)
        finish()

    }

    fun titleSetting() {
        val backText = includeDatePicker.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeDatePicker.findViewById(R.id.title_name) as TextView?
        val titleBarRight = includeDatePicker.findViewById(R.id.titleBarRightText) as TextView?
        titleBarRight?.visibility = View.INVISIBLE
        backText?.text = getString(R.string.HistoricalTx_Filter)
        titleBarText?.text = getString(R.string.TxSort_Date)
        backText!!.setOnClickListener {
            finish()
        }

    }

}

