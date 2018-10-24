package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.models.adapter.ContactsCheckBoxAdapter
import com.xinwang.xinwallet.models.adapter.ContactsHorizontalAdapter
import com.xinwang.xinwallet.models.adapter.IOnBtnClickListen
import com.xinwang.xinwallet.models.adapter.OnItemCheckBoxListen
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_contacts_check_box.*
import kotlinx.android.synthetic.main.activity_tx_filter.*

class ContactsCheckBoxActivity : XinActivity() {

    lateinit var totalContactsList: ArrayList<Contacts>

    var selectedContactsList: MutableList<Contacts> = ArrayList<Contacts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_check_box)
        titleSetting()
        getContactsList()

    }

    fun getContactsList() {
        com.xinwang.xinwallet.jsonrpc.Contacts().getAllContactsList { status, it ->
            if (status) {
                try {
                    totalContactsList = it!!
                    val ad = ContactsCheckBoxAdapter(it!!, this,true)
                    ad.setOnItemCheckBoxListen(object : OnItemCheckBoxListen {
                        override fun onCheckboxChanged(ischecked: Boolean, postion: Int) {
                            itemChanged(ischecked, postion)
                        }
                    })
                    doUI {
                        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        recyclerView.adapter = ad
                    }
                } catch (e: Exception) {
                    doUI { Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    private fun titleSetting() {
        val backText = includeTitlebarContCheckbox.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeTitlebarContCheckbox.findViewById(R.id.title_name) as TextView?
        val titleBarRight = includeTitlebarContCheckbox.findViewById(R.id.titleBarRightText) as TextView?
        val backImage = includeTitlebarContCheckbox.findViewById(R.id.imageBack) as ImageView?
        titleBarText?.text = getText(R.string.HistoricalTx_Filter)
        titleBarRight?.text = getText(R.string.Ok)
        backImage?.setImageResource(R.drawable.ic_coin_btc)
        backImage?.setOnClickListener {
            finish()
        }
        backText?.text = ""
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        titleBarRight!!.setOnClickListener {
            val gson = Gson()
            val selectedTarget = gson.toJson(selectedContactsList)
            val intent = Intent().putExtra("selectedTarget", selectedTarget)
            setResult(10, intent)
            finish()
        }
    }

    private fun itemChanged(isChecked: Boolean, position: Int) {

        selectedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if (isChecked) {
            selectedContactsList!!.add(totalContactsList[position])
        } else {
            val obj: Contacts = selectedContactsList.filter {
                it.userId == totalContactsList[position].userId
            }[0]
            selectedContactsList.remove(obj)
        }
        var selectedAdapter = ContactsHorizontalAdapter(selectedContactsList as ArrayList<Contacts>, this, true)
        selectedAdapter.setOnBtnClickListen(object : IOnBtnClickListen {
            override fun onClickListen(position: Int) {
                uncheckBox(position)
            }
        })
        selectedRecyclerView.adapter = selectedAdapter
    }

    /* uncheckBox
    ＊刪除已選聯絡人
    * selectedPosition:selectedRecyclerView中的位置
    *
    * */
    private fun uncheckBox(selectedPosition: Int) {
        val obj: Contacts = totalContactsList.filter {
            it.userId == selectedContactsList[selectedPosition].userId
        }[0]
        val checkBoxIndex = totalContactsList.indexOf(obj)
        val view = recyclerView.getChildAt(checkBoxIndex)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        checkBox.isChecked = false
    }
}
