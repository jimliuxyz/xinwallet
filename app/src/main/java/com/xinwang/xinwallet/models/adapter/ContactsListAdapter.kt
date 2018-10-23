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
import com.xinwang.xinwallet.models.Contacts
import java.util.ArrayList

class ContactsListAdapter(val  context: Context, data: ArrayList<Contacts>) :
        RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    private var mItem = ArrayList<Contacts>()

    init {
        mItem = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text=mItem[position].name
        Glide.with(context).load(mItem[position].avatar).apply(RequestOptions().centerCrop().circleCrop())
                .into(holder.imgAvatar)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val textName:TextView = itemView.findViewById(R.id.contacts_item_name)
        internal val imgAvatar:ImageView = itemView.findViewById(R.id.contacts_item_image)

    }

}