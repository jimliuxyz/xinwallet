package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Contacts
import com.xinwang.xinwallet.models.adapter.ContactsListAdapter
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_contacts.*


class ContactsActivity : AppCompatActivity() {
    val TAG = "ContactsActivity"
    val loader = LoaderDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        titleSetting()
        getContactsList()
    }

    private fun titleSetting() {
        val backText = include_Contacts.findViewById(R.id.txt_back) as TextView
        val titleBarText = include_Contacts.findViewById(R.id.title_name) as TextView
        val rightText = include_Contacts.findViewById(R.id.titleBarRightText) as TextView

        backText.text=getText(R.string.Cancel)
        titleBarText.text=getText(R.string.Contacts)
        //rightText.d
    }

    fun getContactsList() {
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        Contacts().getContactsList { status, it ->
            loader.dismiss()
            if (status) {
                try {
                    var adapter = ContactsListAdapter(this,it!!)
                    doUI {
                        recyclerViewContactsList.adapter =adapter
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "getContactsList1_$e")
                    doUI { Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show() }
                }
            }

        }
    }

    fun addFriendBtn(view: View) {
        var intent = Intent(this@ContactsActivity, AddFriendActivity::class.java)
        startActivity(intent)
    }

}

