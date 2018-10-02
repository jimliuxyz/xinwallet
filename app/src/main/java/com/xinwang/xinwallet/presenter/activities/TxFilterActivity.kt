package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_tx_filter.*
import java.lang.Exception


class TxFilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tx_filter)
        settingTitleBar()
        radioGroupSetting()
    }


    private fun settingTitleBar() {
        val backText = includeTitlebarTxFilter.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeTitlebarTxFilter.findViewById(R.id.title_name) as TextView?
        val rightText = includeTitlebarTxFilter.findViewById(R.id.titleBarRightText) as TextView?
        backText?.text = getString(R.string.Historical_Transaction)
        titleBarText?.text = getString(R.string.HistoricalTx_Filter)
        rightText?.text = getString(R.string.Btn_done)
        rightText?.setOnClickListener {
            filterDone()
        }

    }

    private fun filterDone() {
        try {
            val TxView: RadioButton = findViewById(RGTxType.checkedRadioButtonId)
            val dateView: RadioButton = findViewById(RGDateType.checkedRadioButtonId)
            val currencyView: RadioButton = findViewById(RGCurrencyType.checkedRadioButtonId)
            val sortingView:RadioButton=findViewById(RGOrder.checkedRadioButtonId)

            println("getTag_${view.getTag()}")
            Toast.makeText(this, sortingView.getTag().toString()+"/"+
                    TxView.getTag().toString() + "/"
                    + dateView.getTag().toString() + "/"
                    + currencyView.getTag().toString(),
                    Toast.LENGTH_LONG).show()
        } catch (e: Exception) {

        }
    }

    private fun radioGroupSetting() {
        RGTxType.setOnCheckedChangeListener { radioGroup, selectedID ->
            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                } else {
                    view.setTextColor(getColor(R.color.gray))
                }

            }
        }

        RGDateType.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                } else {
                    view.setTextColor(getColor(R.color.gray))
                }
            }

        }

        RGCurrencyType.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                } else {
                    view.setTextColor(getColor(R.color.gray))
                }
            }


        }

    }
}
