package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
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
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonArray
import com.xinwang.xinwallet.models.currency
import org.json.JSONArray


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
        getProfileData()
    }



    fun getProfileData() {
        Profile().getProfile {
            var res = JSONObject(it.toString())
            var currencies = ArrayList<currency>()

            val jsonArray=res.getJSONArray("currencies")

            println("currency_${res.toString()}")
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.get(i) as JSONObject
                val currency = currency(jsonObject.getString("name"), jsonObject.getInt("order"),jsonObject.getBoolean("isDefault"))
                //currencies.add(jsonObject.getInt("order"),currency)
                currencies.add(currency)
                if(jsonObject.getBoolean("isDefault")){
                    doUI {
                        default_currency.text=jsonObject.getString("name")
                    //    default_balance.text=jsonObject.getDouble("")
                    }
                }
            }
            val list = currencies .sortedWith(compareBy({ it.order}))

            doUI {
                Glide.with(this).load(res.getString("avatar")).apply(RequestOptions().centerCrop().circleCrop()).into(avatar)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val text = "token:${XinWalletService.instance.getUserToken()}"
        // balance.text = text
    }

    fun btnResetUserData(view: View) {
        XinWalletService.instance.delUserToken()
        exitApp()
    }

    fun balanceBtnClick(view: View) {

        var intent = Intent()
        intent.setClass(this, BalanceListActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

}
