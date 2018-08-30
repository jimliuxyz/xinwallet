package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.Currency
import java.text.NumberFormat
import java.util.*


class HomeActivity : XinActivity() {

    var currencies = ArrayList<Currency>()
    //千位數符號
    val numberFormat = NumberFormat.getNumberInstance()
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.Home_tag_friends)
                friendsActivity()
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
            if (it != null) {
                var res = JSONObject(it.toString())
                val jsonArray = res.getJSONArray("currencies")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.get(i) as JSONObject
                    val currency = Currency(jsonObject.getString("name"), jsonObject.getInt("order"), jsonObject.getBoolean("isDefault"))
                    if (jsonObject.getBoolean("isDefault")) {
                        Trading().getBalances(jsonObject.getString("name")) {
                            doUI {
                                default_balance.text = numberFormat.format(it)
                            }
                        }
                        doUI {
                            default_currency.text = jsonObject.getString("name")
                        }
                    }
                    currencies.add(currency)
                }
                //     val list = currencies .sortedWith(compareBy({ it.order}))
                doUI {
                    Glide.with(this).load(res.getString("avatar")).apply(RequestOptions().centerCrop().circleCrop()).into(avatar)
                }
            }
        }
    }

    fun btnResetUserData(view: View) {
        XinWalletService.instance.delUserToken()
        exitApp()
    }

    fun balanceBtnClick(view: View) {

        var intent = Intent()
        intent.setClass(this, BalanceListActivity::class.java)
        //intent.putStringArrayListExtra("list",)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

    }

    fun friendsActivity() {

        var intent = Intent()
        intent.setClass(this,ContactsActivity::class.java)
        startActivity(intent)
    }

}
