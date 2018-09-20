package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.adapter.RecycleerViewOrderSettingAdapter
import com.xinwang.xinwallet.models.adapter.RecyclerListAdapter
import com.xinwang.xinwallet.models.adapter.helper.OnStartDragListener
import com.xinwang.xinwallet.models.adapter.helper.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_currency_setting.*


class CurrencySettingActivity : AppCompatActivity(), OnStartDragListener {

   private var mItemTouchHelper: ItemTouchHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_setting)
        //var mItemTouchHelper: ItemTouchHelper? = null
        val backText = include.findViewById(R.id.txt_back) as TextView?
        val titleBarText = include.findViewById(R.id.title_name) as TextView?
        backText?.text = getString(R.string.Balance)
        titleBarText?.text = getText(R.string.Balance_setting)
        val adapter = RecycleerViewOrderSettingAdapter(this, this)
        cycleViewerSittingOrder.setHasFixedSize(true)
        cycleViewerSittingOrder.adapter = adapter
        cycleViewerSittingOrder.layoutManager = LinearLayoutManager(this)
        val callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(cycleViewerSittingOrder)

    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper?.startDrag(viewHolder)
    }


}
