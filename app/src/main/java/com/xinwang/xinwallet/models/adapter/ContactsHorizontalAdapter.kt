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

class ContactsHorizontalAdapter(val data: ArrayList<Contacts>, val context: Context, isShowDeleteBtnArg: Boolean) :
        RecyclerView.Adapter<ContactsHorizontalAdapter.ViewHolder>() {
    private var mItems = ArrayList<Contacts>()
    private val isShowDeleteBtn = isShowDeleteBtnArg
    private var onBtnClickListen: IOnBtnClickListen? = null

    init {
        mItems = data
    }

    fun setOnBtnClickListen(listen: IOnBtnClickListen) {
        this.onBtnClickListen = listen
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.listitem_contacts_horizontal, parent, false)
        return ViewHolder(view,onBtnClickListen!!)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //隱藏刪除按鈕
        if (!isShowDeleteBtn) {
            holder.btnDetel.visibility = View.GONE
        }
        holder.textName.text = mItems[position].name
        Glide.with(context).load(mItems[position].avatar).apply(RequestOptions().centerCrop().circleCrop()).into(holder.imageAvatar)
    }

    class ViewHolder internal constructor(itemView: View, btnClickListen: IOnBtnClickListen) : RecyclerView.ViewHolder(itemView) {
        internal val imageAvatar: ImageView = itemView.findViewById(R.id.imageView007)
        internal val textName: TextView = itemView.findViewById(R.id.textView)
        internal val btnDetel: ImageView = itemView.findViewById(R.id.deteltBtn)

        init {
            btnDetel.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    btnClickListen.onClickListen(adapterPosition)
                }
            }
        }
    }
}

interface IOnBtnClickListen {
    fun onClickListen(position: Int)
}