package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.transition.Visibility
import android.view.View
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.models.adapter.RecycleerViewOrderSettingAdapter
import com.xinwang.xinwallet.models.adapter.helper.OnStartDragListener
import com.xinwang.xinwallet.models.adapter.helper.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_currency_setting.*
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.listitem_currency_order_setting.view.*
import org.json.JSONObject


class CurrencySettingActivity : XinActivity(), OnStartDragListener {

    private var mItemTouchHelper: ItemTouchHelper? = null
    private var isDragged: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_setting)
        titleBarSetting()
        val orderList = getPREFCurrencyOrderList()
        orderList.sortBy { it.order }
        val adapter = RecycleerViewOrderSettingAdapter(this, this, orderList)
        cycleViewerSittingOrder.setHasFixedSize(true)
        cycleViewerSittingOrder.adapter = adapter
        cycleViewerSittingOrder.layoutManager = LinearLayoutManager(this)
        val callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(cycleViewerSittingOrder)

    }

    private fun titleBarSetting() {
        val backText = includeTitleBarCurrencySetting.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeTitleBarCurrencySetting.findViewById(R.id.title_name) as TextView?
        val rightText=includeTitleBarCurrencySetting.findViewById(R.id.titleBarRightText) as TextView
        rightText.visibility = View.INVISIBLE
        backText?.text = getString(R.string.Balance)
        titleBarText?.text = getText(R.string.Balance_setting)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper?.startDrag(viewHolder)
        isDragged = true
    }

    fun updateCurrencyOrder(){
        var currencyDragList = ArrayList<Currency>()
        if (isDragged) {
            for (i in 1..cycleViewerSittingOrder.adapter.itemCount) {
                val view = cycleViewerSittingOrder.getChildAt(i - 1)
                if (i - 1 == 0) {
                    currencyDragList.add(Currency(view.cuyEnName.text.toString(), i - 1, true))
                } else {
                    currencyDragList.add(Currency(view.cuyEnName.text.toString(), i - 1, false))
                }
            }
            Profile().updateCurrencySetting(currencyDragList) { status: Boolean?, s: String ->
                if(status!! && JSONObject(s).isNull("error")){
                    saveCurrencyOrderInSharedPreference1()
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        updateCurrencyOrder()
    }

}


