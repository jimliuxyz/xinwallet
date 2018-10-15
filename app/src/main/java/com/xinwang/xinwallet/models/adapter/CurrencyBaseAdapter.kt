package com.xinwang.xinwallet.models.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.currency_item.view.*
import java.text.NumberFormat
import java.util.ArrayList

class CurrencyBaseAdapter(context: Context, currencyList: ArrayList<Currency>?) : BaseAdapter() {
    private val numberFormat = NumberFormat.getNumberInstance()
    private var context: Context? = null
    private var layoutInflater: LayoutInflater? = null
    private var currencyList: ArrayList<Currency>? = null

    init {
        this.context = context
        layoutInflater = LayoutInflater.from(context)
        this.currencyList = currencyList

    }

    override fun getView(postion: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater!!.inflate(R.layout.currency_item, parent, false)
        var coin: Int
        val curName = currencyList!![postion].name
        val txtAmtText=XinActivity().getCurySymbol(curName)+numberFormat.format(XinActivity().getPREFCurrencyBalance(curName))
        view.txtAmountCurrItem.text =txtAmtText
        view.txtNameCurrItem.text = curName
        coin = XinActivity().getCoinIconId(curName)
        view.ImgCurrItem.setImageResource(coin)
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return currencyList!!.size
    }
}