package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import com.bumptech.glide.Glide
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject


import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.bumptech.glide.request.RequestOptions



class HomeActivity : XinActivity() {


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
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        Profile().getProfile{
            var res= JSONObject(it.toString())
            balance.text=res.getString("currencies")
            doUI {
                Glide.with(this).load(res.getString("avatar")).apply(RequestOptions().centerCrop().circleCrop()).into(avatar)
            }
        }



    }


    override fun onResume() {
        super.onResume()

        val text = "token : ${XinWalletService.instance.getUserToken()}"
       // balance.text=text
    }

    fun btnResetUserData(view:View){
        XinWalletService.instance.delUserToken()
        exitApp()
    }



}
