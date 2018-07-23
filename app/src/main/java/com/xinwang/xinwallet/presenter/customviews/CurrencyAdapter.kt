package com.xinwang.xinwallet.presenter.customviews


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.xinwang.xinwallet.R

class CurrencyAdapter(private val context: Context) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(p0: Int): Any {
        return 3
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View {

        val rowView = inflater.inflate(R.layout.currency_item, parent, false)
        val tvCurrencyName = rowView.findViewById(R.id.tvCurrencyName) as TextView
        val tvUnit = rowView.findViewById(R.id.unit) as TextView
        val tvAmount = rowView.findViewById(R.id.amount) as TextView
        val imageView = rowView.findViewById(R.id.imageView) as ImageView
        tvAmount.text = "88"
        tvUnit.text = "bits"
        tvCurrencyName.text = "比特币"

        return rowView
    }
}