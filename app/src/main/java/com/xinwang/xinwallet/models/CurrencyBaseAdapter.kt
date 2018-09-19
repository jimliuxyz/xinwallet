package com.xinwang.xinwallet.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.xinwang.xinwallet.R
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
        view.txtAmountCurrItem.text=numberFormat.format(currencyList!![postion].balance).toString()
        view.txtNameCurrItem.text = curName
        when (curName) {
            "BTC" -> coin = R.drawable.ic_coin_btc
            "USD" -> coin = R.drawable.ic_coin_usd
            "ETH" -> coin = R.drawable.ic_coin_eth
            else -> coin = R.drawable.ic_coin_cny
        }
        view.ImgCurrItem.setImageResource(coin)
        // Glide.with(view).load(contactsList!![position].avatar).into(view.contacts_item_image)
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