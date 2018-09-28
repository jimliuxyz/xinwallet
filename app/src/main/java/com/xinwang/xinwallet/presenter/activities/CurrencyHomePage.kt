package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.models.adapter.HistoryTxListAdapter
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_currency_home_page.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import android.support.v7.widget.LinearLayoutManager
import com.xinwang.xinwallet.tools.util.doUI


class CurrencyHomePage : XinActivity() {

    var currencyName: String = ""
    var currencyAmount: String = ""
    val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_home_page)
        currencyName = intent.getStringExtra("currName")
        currencyAmount = intent.getStringExtra("currAmount")
        titleBarSetting()
        txtCurrencyAmount.text = currencyAmount
        imgCoin.setImageResource(getCoinIconId(currencyName))
        getHistoryList()
    }

    private fun getHistoryList() {
        Trading().getReceipts(Date()) { state: Boolean, result: String ->
            val jsonArrayString = JSONObject(result).getJSONArray("list").toString()
            val founderArray = gson.fromJson(jsonArrayString, Array<TransactionRecord>::class.java)
            val theCurrencyTx = founderArray.filter { TransactionRecord ->
                TransactionRecord.currency == currencyName
            }.toCollection(ArrayList())
            showHistoryList(theCurrencyTx)
        }
    }

    private fun showHistoryList(founderArray: ArrayList<TransactionRecord>?) {
        doUI {
            recyclerView_HistoryTrx.layoutManager = LinearLayoutManager(this)
            recyclerView_HistoryTrx.setHasFixedSize(true)
            recyclerView_HistoryTrx.adapter = HistoryTxListAdapter(founderArray!!, this)
        }
    }

    private fun titleBarSetting() {
        val backText = includeTitleBarCurrencyHome.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeTitleBarCurrencyHome.findViewById(R.id.title_name) as TextView?
        val titleBarRight = includeTitleBarCurrencyHome.findViewById(R.id.titleBarRightText) as TextView?
        titleBarRight?.visibility = View.INVISIBLE
        backText?.text = getString(R.string.Balance)
        titleBarText?.text = getString(getCurrencyNameStringId(currencyName)) + getString(R.string.Balance)
    }

    private fun getCurrencyNameStringId(name: String): Int {
        when (name) {
            "BTC" -> return (R.string.Currency_BTC)
            "USD" -> return (R.string.Currency_USD)
            "ETH" -> return (R.string.Currency_ETH)
            else -> return (R.string.Currency_CNY)
        }

    }
}
