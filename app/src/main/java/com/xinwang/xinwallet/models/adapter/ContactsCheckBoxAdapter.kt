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

class ContactsCheckBoxAdapter(val data: ArrayList<Contacts>, val context: Context) :
        RecyclerView.Adapter<ContactsCheckBoxAdapter.ViewHolder>() {

    private var mItems = ArrayList<Contacts>()

    init {
        mItems = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem_constacts_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textName.text = mItems[position].name
        Glide.with(context).load(mItems[position].avatar).apply(RequestOptions().centerCrop().circleCrop()).into(holder.imageAvatar)

    }

    override fun getItemCount(): Int {
        return mItems.size
    }


    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val imageAvatar: ImageView = itemView.findViewById(R.id.imageView)
        internal val textName: TextView = itemView.findViewById(R.id.textViewName)
        internal val check_Box: CheckBox = itemView.findViewById(R.id.checkBox)

    }
}