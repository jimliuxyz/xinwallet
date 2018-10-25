package com.xinwang.xinwallet.models.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.Contacts

class ContactsCheckBoxAdapter(val data: ArrayList<Contacts>, val context: Context,val showCheckBox:Boolean) :
        RecyclerView.Adapter<ContactsCheckBoxAdapter.ViewHolder>() {


    private var mItems = ArrayList<Contacts>()
    private var onItemCheckBoxListen: OnItemCheckBoxListen? = null
    private var onItemClickListen:OnItemClickListen?=null

    fun setOnItemCheckBoxListen(listen: OnItemCheckBoxListen) {
        this.onItemCheckBoxListen = listen
    }
    fun setOnItemClickListen(listen:OnItemClickListen){
        this.onItemClickListen=listen
    }

    init {
        mItems = data
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem_contacts_checkbox, parent, false)
        return ViewHolder(view, onItemCheckBoxListen!!,onItemClickListen!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       //不顯示checkBox
        if (!showCheckBox){
            holder.checkBox.visibility=View.GONE
        }
        holder.textName.text = mItems[position].name
        Glide.with(context).load(mItems[position].avatar).apply(RequestOptions().centerCrop().circleCrop()).into(holder.imageAvatar)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder internal constructor(itemView: View, onItemCheckBoxListen: OnItemCheckBoxListen,
                                          onItemClickListen: OnItemClickListen)
        : RecyclerView.ViewHolder(itemView) {
        internal val imageAvatar: ImageView = itemView.findViewById(R.id.imageView007)
        internal val textName: TextView = itemView.findViewById(R.id.textViewName)
        internal val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)


        init {
            checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemCheckBoxListen.onCheckboxChanged(isChecked, adapterPosition)
                }
            }

            itemView.setOnClickListener {
                if (adapterPosition!=RecyclerView.NO_POSITION){
                    onItemClickListen.onItemClick(adapterPosition)
                }
            }

        }
    }
}

interface OnItemCheckBoxListen {
    fun onCheckboxChanged(isChecked: Boolean, position: Int)
}

interface OnItemClickListen{
    fun onItemClick(position: Int)
}