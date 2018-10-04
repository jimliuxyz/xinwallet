package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.models.adapter.ContactsCheckBoxAdapter
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_contacts_check_box.*

class ContactsCheckBoxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_check_box)
        titleSetting()
        //loadContactsList()
        getContactsList()

    }

    private fun loadContactsList() {
        val data: ArrayList<Contacts> = ArrayList()
        data.add(Contacts("qwfd"))
        data.add(Contacts("qdfgw"))
        data.add(Contacts("qw"))
        data.add(Contacts("qgfhthw"))
        data.add(Contacts("qwtry53"))
        data.add(Contacts("qdfgw"))
        data.add(Contacts("qw"))
        data.add(Contacts("qgfhthw"))
        data.add(Contacts("qwtry53"))
        data.add(Contacts("qdfgw"))
        data.add(Contacts("qw"))
        data.add(Contacts("qgfhthw"))
        data.add(Contacts("qwtry53"))
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ContactsCheckBoxAdapter(data, this)

    }


    fun getContactsList() {
        com.xinwang.xinwallet.jsonrpc.Contacts().getAllContactsList { status, it ->
            // loader.dismiss()
            if (status) {
                try {
                    doUI {
                        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        recyclerView.adapter = ContactsCheckBoxAdapter(it!!, this)
                    }
                } catch (e: Exception) {
                    Log.i("8989898", "getContactsList1_$e")
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
        backText?.text = ""
        recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

    }
}
