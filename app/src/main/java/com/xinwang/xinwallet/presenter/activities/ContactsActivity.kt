package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Contacts
import com.xinwang.xinwallet.tools.util.doUI


class ContactsActivity : AppCompatActivity() {

    lateinit var contactsList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        contactsList=findViewById(R.id.contactsList)
        getContactsList()
    }

    fun getContactsList() {
        var sste=ArrayList<String>()
        Contacts().getContactsList {
            it!!.forEach {
                sste.add(it.name)
            }

            var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,sste)

            doUI {
                contactsList.adapter=adapter
            }


        }

    }

}

