package com.xinwang.xinwallet.presenter.activities


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.models.adapter.HistoryTxListAdapter
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_currency_home_page.*
import kotlinx.android.synthetic.main.activity_history_tx.*
import org.json.JSONObject
import java.util.*

class HistoricalTxActivity : XinActivity() {

    var fromActivity: String? = null
    val gson = Gson()
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
            recyclerViewHistoryTx.layoutManager = LinearLayoutManager(this@HistoricalTxActivity)
            recyclerViewHistoryTx.setHasFixedSize(true)
            var historyAdapter = HistoryTxListAdapter(founderArray!!, this@HistoricalTxActivity)
            historyAdapter.setOnItemClickLisrten(object : HistoryTxListAdapter.OnitemClickListener {
                override fun onItemClick(poistion: Int,view:View) {
                    var amount:TextView=view.findViewById(R.id.txt_Amount)
                    var titletText:TextView=view.findViewById(R.id.txt_title)
                    val intent = Intent(this@HistoricalTxActivity, TxDetailActivity::class.java)
                    intent.putExtra("amount",amount.text)
                    intent.putExtra("target",titletText.text.subSequence(3,titletText.text.length))
                    intent.putExtra("obj",Gson().toJson(founderArray[poistion]))
                    startActivity(intent)
                }

            })
            recyclerViewHistoryTx.adapter = historyAdapter

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
        rightText.setOnClickListener {
            val intent = Intent(this, TxFilterActivity::class.java)
            startActivityForResult(intent, 0)
        }
        backText.setOnClickListener {
            finish()
        }


    }
}

