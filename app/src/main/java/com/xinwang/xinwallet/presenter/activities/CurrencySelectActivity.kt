package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.adapter.RecyclerViewOrderSettingAdapter
import com.xinwang.xinwallet.models.adapter.helper.OnStartDragListener
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_currency_select.*
import com.xinwang.xinwallet.R.id.recyclerView


class CurrencySelectActivity : XinActivity(), OnStartDragListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_select)
        titleSetting()
        loadData()
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        val listMas = recyclerView.adapter.itemCount - 1
        if (viewHolder!!.adapterPosition >= 0) {
            for (i in 0..listMas) {
                val view = recyclerView.findViewHolderForAdapterPosition(i).itemView
                val img = view.findViewById(R.id.curyImg2) as ImageView
                when (i) {
                    viewHolder.adapterPosition -> {
                        img.setImageResource(R.drawable.checkmark)
                        img.visibility = View.VISIBLE
                    }
                    else -> {
                        img.visibility = View.GONE
                    }
                }
            }
            val backIntent=Intent().putExtra("selectedCury",viewHolder!!.adapterPosition)
            setResult(RESULT_CODE,backIntent)
            finish()
        }
    }

    private fun loadData() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerViewOrderSettingAdapter(this, this, getPREFCurrencyOrderList())
        recyclerView.adapter = adapter
        val listMas = recyclerView.adapter.itemCount - 1
        //勾選預設幣別
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        for (i in 0..listMas) {
                            val view = recyclerView.getChildAt(i)
                            val img = view.findViewById(R.id.curyImg2) as ImageView
                            when (i) {
                                intent.getIntExtra("selectedCury", 0) -> {
                                    img.setImageResource(R.drawable.checkmark)
                                    img.visibility = View.VISIBLE
                                }
                                else -> {
                                    img.visibility = View.GONE
                                }
                            }
                        }
                        recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    }

                })
    }

    private fun titleSetting() {
        val backText = includeCurySelect.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeCurySelect.findViewById(R.id.title_name) as TextView
        val rightText = includeCurySelect.findViewById(R.id.titleBarRightText) as TextView
        backText.text = getText(R.string.Cancel)
        backText.setOnClickListener { finish() }
        titleBarText.text = getText(R.string.Exchange)
        rightText.visibility = View.GONE
    }
}
