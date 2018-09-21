package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.xinwang.xinwallet.R
import android.widget.SimpleAdapter
import android.widget.TextView
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.models.CurrencyBaseAdapter
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_balance_list.*
import java.text.NumberFormat


//千位數符號

class BalanceListActivity : XinActivity() {

    private val numberFormat = NumberFormat.getNumberInstance()
    private var defaultName: TextView? = null
    private var defaultAmount: TextView? = null
    private var defaultImage: ImageView? = null
    var arrayList: ArrayList<Currency> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_list)
        defaultName = include2.findViewById(R.id.txtNameCurrItem)
        defaultAmount = include2.findViewById(R.id.txtAmountCurrItem)
        defaultImage = include2.findViewById(R.id.ImgCurrItem)
        getCurrencyList()
    }

    fun getCurrencyList() {
        val list = ArrayList<Map<String, Any>>()
        val s1 = arrayOf("currencyName", "amount")
        val s2: IntArray = intArrayOf(R.id.txtNameCurrItem, R.id.txtAmountCurrItem)
        Trading().getBalancesList {
            if (it != null && !it.isEmpty()) {
                it!!.forEach {
                    var map: MutableMap<String, Any> = HashMap()
                    map["currencyName"] = it.name
                    map["amount"] = numberFormat.format(it.balance)
                    list.add(map)
                    //add object
                    arrayList.add(it)
                }
                doUI {
                    val firstCurName = arrayList[0].name
                    defaultName!!.text = firstCurName
                    defaultAmount!!.text = numberFormat.format(arrayList[0].balance).toString()
                    defaultImage!!.setImageResource(getCoinIconId(firstCurName))
                    list.removeAt(0)
                    arrayList.removeAt(0)

                    //val adapter = SimpleAdapter(this, list, R.layout.currency_item, s1, s2)
                    val adapter = CurrencyBaseAdapter(this, arrayList)
                    currencyList.adapter = adapter
                }
            }
        }
    }

    fun settingOnclick(view: View) {
        val intent= Intent(this,CurrencySettingActivity::class.java)
        startActivity(intent)
    }


}
