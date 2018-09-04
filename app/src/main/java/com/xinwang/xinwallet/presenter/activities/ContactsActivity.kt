package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Contacts
import com.xinwang.xinwallet.models.ContactsBaseAdapter
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_contacts.*


class ContactsActivity : AppCompatActivity() {

    val loader = LoaderDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        getContactsList()
    }

    fun getContactsList() {

        var sste=ArrayList<String>()
        Contacts().getContactsList {
            it!!.forEach {
                sste.add(it.name)
                println(it.name)
            }
           // var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,sste)
            var adapter1 =ContactsBaseAdapter(this,it)
            doUI {
                contactsList.adapter=adapter1
                loader.dismiss()
            }
        }
    }

}

