package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_exchange.*

class ExchangeActivity : XinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)
        titleSetting()
        getSpinnerData()
    }

    private fun getSpinnerData() {
        val orderList = getPREFCurrencyOrderList()

        println("getSpinnerData_$orderList")
    }

    private fun titleSetting() {
        val backText = includeExchange.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeExchange.findViewById(R.id.title_name) as TextView
        val rightText = includeExchange.findViewById(R.id.titleBarRightText) as TextView

        backText.text = getText(R.string.app_name)
        backText.setOnClickListener {
            finish()
        }
        titleBarText.text = getText(R.string.Exchange)
        rightText.visibility = View.GONE
    }
}
