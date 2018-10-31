package com.xinwang.xinwallet.models.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.BankAccount

class BankAccountAdapter(private val dataList: ArrayList<BankAccount>, val context: Context)
    : RecyclerView.Adapter<BankAccountAdapter.ViewHolder>() {

    private  var onItemSelectedBtnListen:OnItemSelectedBtnListen?=null


    fun setOnSelectedBtnListen(listen:OnItemSelectedBtnListen){
        onItemSelectedBtnListen=listen
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem_bank_account, parent, false)
        return ViewHolder(view,onItemSelectedBtnListen!!)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataList[position].backName
        holder.subTitle.text = dataList[position].accountNo
    }


    class ViewHolder internal constructor(itemView: View, onItemSelectedBtnListen: OnItemSelectedBtnListen)
        : RecyclerView.ViewHolder(itemView) {

        internal val title: TextView = itemView.findViewById(R.id.tvBankName)
        internal val subTitle: TextView = itemView.findViewById(R.id.tvAccount)
        internal val img: ImageView = itemView.findViewById(R.id.img)
        private val selectBtn: Button = itemView.findViewById(R.id.btnSelected)


        init {
            selectBtn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    onItemSelectedBtnListen.onSelectedClick(adapterPosition)
            }
        }

    }
}

interface OnItemSelectedBtnListen {

    fun onSelectedClick(position: Int)
}


