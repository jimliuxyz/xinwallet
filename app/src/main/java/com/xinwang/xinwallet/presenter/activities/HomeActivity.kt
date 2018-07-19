package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.ListView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.customviews.CurrencyAdapter
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : XinActivity() {

    private lateinit var listView :ListView

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.Home_tag_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.Home_tag_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.Home_tag_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.currency_item)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadingListView()

    }

   fun  loadingListView(){
       listView = findViewById<ListView>(R.id.listView)
       val adapter = CurrencyAdapter(this)
       listView.adapter=adapter

    }

    override fun onResume() {
        super.onResume()

        val text = "token : ${XinWalletService.instance.getUserToken()}"
        tvInfo.setText(text)
    }

    fun btnResetUserData(view:View){
        XinWalletService.instance.delUserToken()
        exitApp()
    }



}
