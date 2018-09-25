package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_currency_setting.*

class CurrencyHomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_home_page)
        val currName= intent.getStringExtra("currName")
        titleBarSetting(currName)

    }

    private fun titleBarSetting(currenctName: String) {
        val backText = include.findViewById(R.id.txt_back) as TextView?
        val titleBarText = include.findViewById(R.id.title_name) as TextView?
        backText?.text = getString(R.string.Balance)
        //TODO("放入各幣別名稱")
        titleBarText?.text =currenctName
    }
}
