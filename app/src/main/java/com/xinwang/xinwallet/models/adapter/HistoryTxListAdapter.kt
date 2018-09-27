package com.xinwang.xinwallet.models.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.tools.util.doUI


class HistoryTxListAdapter(val data: ArrayList<TransactionRecord>, val context: Context)
    : RecyclerView.Adapter<HistoryTxListAdapter.ViewHolder>() {

    private var mItems = ArrayList<TransactionRecord>()

    init {
        mItems = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.listitem_history_deal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = mItems[position].txType.toString()
        holder.txtAmount.text = mItems[position].txResult.amount.toString()
        holder.txtDate.text = mItems[position].datetime.toString()
        val userId = mItems[position].txParams.receiver
        Profile().getUsersProfile(arrayOf(userId)) { status: Boolean, resultArrayList: ArrayList<Contacts> ->
            if (status) {
                doUI {
                    holder.txtTitle.text = resultArrayList[0].name
                    Glide.with(context).load(resultArrayList[0].avatar).apply(RequestOptions().centerCrop().circleCrop())
                            .into(holder.imgAvatar)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val txtAmount: TextView = itemView.findViewById(R.id.txt_Amount)
        internal val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        internal val imgAvatar: ImageView = itemView.findViewById(R.id.img_Avatar)
        internal val txtDate: TextView = itemView.findViewById(R.id.txt_date)

    }


}


