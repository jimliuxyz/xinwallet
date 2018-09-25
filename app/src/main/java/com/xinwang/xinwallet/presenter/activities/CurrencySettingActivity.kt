package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.models.adapter.RecycleerViewOrderSettingAdapter
import com.xinwang.xinwallet.models.adapter.RecyclerListAdapter
import com.xinwang.xinwallet.models.adapter.helper.OnStartDragListener
import com.xinwang.xinwallet.models.adapter.helper.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_currency_setting.*
import com.google.gson.Gson
import com.xinwang.xinwallet.tools.util.getPref
import com.google.gson.reflect.TypeToken
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.presenter.activities.util.XinActivity


class CurrencySettingActivity : XinActivity(), OnStartDragListener {

    private var mItemTouchHelper: ItemTouchHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_setting)
        titleBarSetting()
        //getList()
        val adapter = RecycleerViewOrderSettingAdapter(this, this, getList())
        cycleViewerSittingOrder.setHasFixedSize(true)
        cycleViewerSittingOrder.adapter = adapter
        cycleViewerSittingOrder.layoutManager = LinearLayoutManager(this)
        val callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(cycleViewerSittingOrder)

    }

    private fun titleBarSetting() {
        val backText = include.findViewById(R.id.txt_back) as TextView?
        val titleBarText = include.findViewById(R.id.title_name) as TextView?
        backText?.text = getString(R.string.Balance)
        titleBarText?.text = getText(R.string.Balance_setting)
    }

    private fun getList(): ArrayList<Currency> {
        val obj = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_ORDER, "")
        val type = object : TypeToken<ArrayList<Currency>>() {}.type
        //  val result = Gson().fromJson<ArrayList<Currency>>(obj,type) //解析
        // println(result.toString())
        return Gson().fromJson<ArrayList<Currency>>(obj, type)
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper?.startDrag(viewHolder)
    }


}
