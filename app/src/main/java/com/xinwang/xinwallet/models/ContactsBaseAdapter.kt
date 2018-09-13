package com.xinwang.xinwallet.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.contacts_item.view.*
import java.util.ArrayList

class ContactsBaseAdapter(context: Context, contactsList: ArrayList<Contacts>?) : BaseAdapter() {

    private var context: Context? = null
    private var layoutInflater: LayoutInflater? = null
    private var contactsList: ArrayList<Contacts>? = null

    init {
        this.context = context
        layoutInflater = LayoutInflater.from(context)
        this.contactsList = contactsList
    }

    override fun getCount(): Int {
        return contactsList!!.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getItem(p0: Int): Any {
        return contactsList!!.get(p0)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater!!.inflate(R.layout.contacts_item, parent, false)
        view.contacts_item_name.text = contactsList!![position].name
        Glide.with(view).load(contactsList!![position].avatar).into(view.contacts_item_image)
        return view
    }


}