package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.xinwang.xinwallet.R
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.models.CurrencyBaseAdapter
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_balance_list.*
import java.text.NumberFormat
import com.xinwang.xinwallet.MainActivity
import kotlinx.android.synthetic.main.currency_item.view.*


//千位數符號

class BalanceListActivity : XinActivity() {

    private val numberFormat = NumberFormat.getNumberInstance()
    private var defaultName: TextView? = null
    private var defaultAmount: TextView? = null
    private var defaultImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_list)
        defaultName = include.findViewById(R.id.txtNameCurrItem)
        defaultAmount = include.findViewById(R.id.txtAmountCurrItem)
        defaultImage = include.findViewById(R.id.ImgCurrItem)
        getCurrencyList()
    }


    fun getCurrencyList() {
        val firstCurName = getPREFCurrencyOrderList().filter { it.isDefault }[0].name
        defaultName!!.text = firstCurName
        defaultAmount!!.text = numberFormat.format(getPREFCurrencyBalance(firstCurName)).toString()
        defaultImage!!.setImageResource(getCoinIconId(firstCurName))

        //非預設幣別清單
        val orderList = getPREFCurrencyOrderList()
        orderList.sortBy { it.order }
        orderList.removeAt(0)
        val adapter = CurrencyBaseAdapter(this, orderList)
        currencyList.adapter = adapter
        currencyList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent=Intent(this,CurrencyHomePage::class.java)
            intent.putExtra("currName",view.txtNameCurrItem.text)
            startActivity(intent)
            Toast.makeText(this, view.txtNameCurrItem.text, Toast.LENGTH_SHORT).show()
        }

    }

    fun settingOnclick(view: View) {
        val intent = Intent(this, CurrencySettingActivity::class.java)
        startActivity(intent)
    }


}
