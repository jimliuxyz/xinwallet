package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Contacts
import com.xinwang.xinwallet.models.ContactsBaseAdapter
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_contacts.*


class ContactsActivity : AppCompatActivity() {
    val TAG = "ContactsActivity"
    val loader = LoaderDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        getContactsList()
    }

    override fun onResume() {
        super.onResume()
        getContactsList()
    }

    fun getContactsList() {
        Contacts().getContactsList { status, it ->
            loader.dismiss()
            if (status) {
                try {
                    var adapter1 = ContactsBaseAdapter(this, it)
                    doUI {
                        contactsList.adapter = adapter1
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

