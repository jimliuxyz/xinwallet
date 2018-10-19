package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_tx_detail.*
import java.text.NumberFormat
import java.text.SimpleDateFormat


class TxDetailActivity : XinActivity() {
    private val numberFormat = NumberFormat.getNumberInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tx_detail)
        titleSetting()
        val type = object : TypeToken<TransactionRecord>() {}.type
        val obj = Gson().fromJson<TransactionRecord>(intent.getStringExtra("obj"), type)
        showDetail(obj)
    }

    private fun showDetail(obj: TransactionRecord?) {
        if (obj != null) {
            val format = SimpleDateFormat("yyyyMMdd HH:mm:ss")
            val defaultAmtText = getCurySymbol(obj.currency) + numberFormat.format(obj.txResult.amount).toString()
            tvTime.text = format.format(obj.datetime)
            tvTxAction.text = obj.getTxTypeName()
            tvAmount.text = intent.getStringExtra("amount")
            tvCurrency.text = obj.currency
            //交易對象
            tvCounterParty.text = intent.getStringExtra("target")
            //交易狀態代碼
            when (obj.statusCode.toInt()) {
                0 -> tvStatus.text = getString(R.string.TxDetail_Success)
                else -> tvStatus.text = getString(R.string.TxDetail_Uncompleted)
            }


        }

    }

    private fun titleSetting() {
        val backText = includeTxDetail.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeTxDetail.findViewById(R.id.title_name) as TextView
        val rightText = includeTxDetail.findViewById(R.id.titleBarRightText) as TextView
        backText.text = getString(R.string.Historical_Transaction)
        titleBarText.text = getText(R.string.TxDetail)
        rightText.text = ""
        backText.setOnClickListener {
            finish()
        }
    }
}
