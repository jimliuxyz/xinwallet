package com.xinwang.xinwallet.models.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import java.text.Format
import java.text.SimpleDateFormat


class HistoryTxListAdapter(val data: ArrayList<TransactionRecord>, val context: Context)
    : RecyclerView.Adapter<HistoryTxListAdapter.ViewHolder>() {
    var listener:OnitemClickListener? = null

    fun setOnItemClickLisrten(onListener: OnitemClickListener){
        listener=onListener
    }

    private var mItems = ArrayList<TransactionRecord>()

    init {
        mItems = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.listitem_history_deal, parent, false)
        return ViewHolder(view,listener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var txtAmount: String = ""
        var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
        var userId: String = ""
        when (mItems[position].txType) {
            3.toShort() -> {
                if (mItems[position].txResult.outflow!!) {
                    txtAmount = "- " +
                            XinActivity().getCurySymbol(mItems[position].currency) + " " +
                            mItems[position].txResult.amount.toString()
                    holder.txtAmount.setTextColor(XinWalletApp.instance.getColor(R.color.red_text))
                    userId = mItems[position].txParams.receiver
                } else {
                    txtAmount = "+ " +
                            XinActivity().getCurySymbol(mItems[position].currency) + " " +
                            mItems[position].txResult.amount.toString()
                    holder.txtAmount.setTextColor(XinWalletApp.instance.getColor(R.color.green_text))
                    userId = mItems[position].txParams.sender
                }
            }
        }
        holder.txtAmount.text = txtAmount
        holder.txtDate.text = sdf.format(mItems[position].datetime)
        val txTypeName = mItems[position].getTxTypeName()
        Profile().getUsersProfile(arrayOf(userId)) { status: Boolean, resultArrayList: ArrayList<Contacts> ->
            if (status) {
                doUI {
                    holder.txtTitle.text = txTypeName + resultArrayList[0].name
                    Glide.with(context).load(resultArrayList[0].avatar).apply(RequestOptions().centerCrop().circleCrop())
                            .into(holder.imgAvatar)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder internal constructor(itemView: View,onitemClickListener: OnitemClickListener) : RecyclerView.ViewHolder(itemView) {
        internal val txtAmount: TextView = itemView.findViewById(R.id.txt_Amount)
        internal val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        internal val imgAvatar: ImageView = itemView.findViewById(R.id.img_Avatar)
        internal val txtDate: TextView = itemView.findViewById(R.id.txt_date)
        init {
            itemView.setOnClickListener(object :View.OnClickListener {
                override fun onClick(view: View?) {
                    if (onitemClickListener!=null){
                        var poistion:Int=adapterPosition
                        if (poistion!=RecyclerView.NO_POSITION){
                            onitemClickListener.onItemClick(poistion,view!!)
                        }
                    }
                }

            })
        }
    }

    interface OnitemClickListener {
        fun onItemClick(poistion: Int,ItemView:View)
    }
}


