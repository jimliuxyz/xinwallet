package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xinwang.xinwallet.R
import android.widget.SimpleAdapter
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_balance_list.*
import java.text.NumberFormat


class BalanceListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_list)
        getCurrency()

    }


    fun getCurrency() {
        val list = ArrayList<Map<String, Any>>()
        val s1 = arrayOf("currencyName", "amount")
        val s2: IntArray = intArrayOf(R.id.text1, R.id.text2)

        Trading().getBalances {
            if (it != null && !it.isEmpty()) {
                val numberFormat = NumberFormat.getNumberInstance()
                it!!.forEach {
                    var map: MutableMap<String, Any> = HashMap()
                    map["currencyName"] = it.name
                    map["amount"] = numberFormat.format(it.balance)
                    list.add(map)
                }
                doUI {
                    val adapter = SimpleAdapter(this, list, R.layout.currency_item, s1, s2)
                    currencyList.adapter = adapter
                }
            }
        }
    }


}