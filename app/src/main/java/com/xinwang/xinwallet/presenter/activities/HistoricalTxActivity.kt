package com.xinwang.xinwallet.presenter.activities


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.google.gson.Gson
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.models.adapter.HistoryTxListAdapter
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_history_tx.*
import org.json.JSONObject
import java.util.*

class HistoricalTxActivity : XinActivity(){

    var fromActivity: String? = null
    val gson =Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_tx)
        fromActivity = intent.getStringExtra("from")
        var txFilter = intent.getStringExtra("txFilter")
        titleBarSetting()
        getTxList()

    }

    private fun getTxList() {
        Trading().getReceipts(Date()) { state: Boolean, result: String ->
            val jsonArrayString = JSONObject(result).getJSONArray("list").toString()
            val founderArray = gson.fromJson(jsonArrayString, Array<TransactionRecord>::class.java)
            val theCurrencyTx = founderArray.filter {
               true
            }.toCollection(ArrayList())
            showHistoryList(theCurrencyTx)
        }
    }


    private fun showHistoryList(founderArray: ArrayList<TransactionRecord>?) {
        doUI {
            recyclerViewHistoryTx.layoutManager = LinearLayoutManager(this)
            recyclerViewHistoryTx.setHasFixedSize(true)
            recyclerViewHistoryTx.adapter = HistoryTxListAdapter(founderArray!!, this)
        }
    }

    private fun titleBarSetting() {
        val backText = includeHistoryTx.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeHistoryTx.findViewById(R.id.title_name) as TextView
        val rightText = includeHistoryTx.findViewById(R.id.titleBarRightText) as TextView
        titleBarText.text = getText(R.string.Historical_Transaction)
        rightText.text = getText(R.string.HistoricalTx_Filter)
        if (fromActivity.equals("Home")) {
            backText.text = getText(R.string.app_name)
        } else {
            backText.text = getText(R.string.app_name)
        }
    }
}

