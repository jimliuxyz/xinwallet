package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.jsonrpc.Contacts
import com.xinwang.xinwallet.models.adapter.ContactsCheckBoxAdapter
import com.xinwang.xinwallet.models.adapter.OnItemCheckBoxListen
import com.xinwang.xinwallet.models.adapter.OnItemClickListen
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_contacts.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ContactsActivity : XinActivity() {
    val TAG = "ContactsActivity"
    val loader = LoaderDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        titleSetting()
        getContactsList()
        //EventBus subscriber
        EventBus.getDefault().register(this@ContactsActivity)
    }

    private fun titleSetting() {
        val backText = include_Contacts.findViewById(R.id.txt_back) as TextView
        val titleBarText = include_Contacts.findViewById(R.id.title_name) as TextView
        val rightText = include_Contacts.findViewById(R.id.titleBarRightText) as TextView

        backText.text = getText(R.string.Cancel)
        backText.setOnClickListener {
            finish()
        }
        titleBarText.text = getText(R.string.Contacts)
        rightText.text = "+"
        rightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f)
        rightText.setTextColor(Color.BLUE)
        rightText.setOnClickListener {
            addFriendBtn()
        }
    }

    fun getContactsList() {
        Contacts().getContactsList { status, it ->
            doUI {
                loader.dismiss()
            }
            if (status) {
                try {
                    var adapter = ContactsCheckBoxAdapter(it!!, this, false)
                    adapter.setOnItemCheckBoxListen(object : OnItemCheckBoxListen {
                        override fun onCheckboxChanged(isChecked: Boolean, position: Int) {
                        }
                    })
                    adapter.setOnItemClickListen(object : OnItemClickListen {
                        override fun onItemClick(position: Int) {
                            val intent=Intent(this@ContactsActivity,ContactDetailActivity::class.java)
                            intent.putExtra("avatar",it[position].avatar)
                            intent.putExtra("userId",it[position].userId)
                            intent.putExtra("name",it[position].name)
                            intent.putExtra("phone",it[position].phoneno)
                            Toast.makeText(this@ContactsActivity, it[position].userId, Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }

                    })
                    doUI {
                        if (it!!.size > 0) {
                            imgEmpty.visibility = View.GONE
                        } else {
                            imgEmpty.visibility = View.VISIBLE
                        }
                        recyclerViewContactsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        recyclerViewContactsList.adapter = adapter
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "getContactsList1_$e")
                    doUI { Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    fun addFriendBtn() {
        var intent = Intent(this@ContactsActivity, AddFriendActivity::class.java)
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: DataUpdateEvent) {
        when (event.type) {
            DataUpdateEvent.FRIENDS_LIST -> getContactsList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@ContactsActivity)
    }

}

