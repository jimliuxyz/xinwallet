/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xinwang.xinwallet.models.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.models.adapter.helper.ItemTouchHelperAdapter
import com.xinwang.xinwallet.models.adapter.helper.ItemTouchHelperViewHolder
import com.xinwang.xinwallet.models.adapter.helper.OnStartDragListener
import com.xinwang.xinwallet.presenter.activities.util.XinActivity

import java.util.ArrayList
import java.util.Collections

/**
 * Simple RecyclerView.Adapter that implements [ItemTouchHelperAdapter] to respond to move and
 * dismiss events from a [android.support.v7.widget.helper.ItemTouchHelper].
 *
 * @author Paul Burke (ipaulpro)
 */
class RecyclerViewOrderSettingAdapter(context: Context, private val mDragStartListener: OnStartDragListener,data:ArrayList<Currency>)
    : RecyclerView.Adapter<RecyclerViewOrderSettingAdapter.ItemViewHolder>(), ItemTouchHelperAdapter {


    private var mItems = ArrayList<Currency>()

    init {
        mItems=data
        mItems.sortWith(compareBy{it.order})
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewOrderSettingAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem_currency_order_setting, parent, false)
        return RecyclerViewOrderSettingAdapter.ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewOrderSettingAdapter.ItemViewHolder, position: Int) {

        holder.currencyEnName.text = mItems[position].name
        holder.currencyIcon.setImageResource(XinActivity().getCoinIconId(mItems[position].name))
        var coinNameId: Int
        when (mItems[position].name) {
            "BTC" -> coinNameId = R.string.Currency_BTC
            "USD" ->coinNameId = R.string.Currency_USD
            "ETH" ->coinNameId =R.string.Currency_ETH
            else -> coinNameId = R.string.Currency_CNY
        }
        holder.currencyName.setText(coinNameId)
        holder.itemView.setOnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder)
            }
            false
        }
    }

    override fun onItemDismiss(position: Int) {
        mItems.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(mItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    /**
     * Simple example of a view holder that implements [ItemTouchHelperViewHolder] and has a
     * "handle" view that initiates a drag event when touched.
     */
    class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {

        internal val currencyEnName: TextView
        internal val currencyName: TextView
        internal val currencyIcon: ImageView


        init {
            currencyEnName = itemView.findViewById(R.id.cuyEnName)
            currencyIcon = itemView.findViewById(R.id.cuyImg)
            currencyName = itemView.findViewById(R.id.cuyName)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }
}
