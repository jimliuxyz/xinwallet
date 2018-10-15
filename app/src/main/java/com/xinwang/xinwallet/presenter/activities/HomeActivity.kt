package com.xinwang.xinwallet.presenter.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.jsonrpc.FuncApp
import com.xinwang.xinwallet.jsonrpc.JSONRPC
import com.xinwang.xinwallet.tools.photo.UriUtil
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : XinActivity() {

    private val loader = LoaderDialogFragment()
    private val loader_balance = LoaderDialogFragment()
    val TAG = "HomeActivity"
    var currencies = ArrayList<Currency>()
    //千位數符號
    val numberFormat = NumberFormat.getNumberInstance()
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //loadingDate
        loadProfileData()
        loadBalanceData()
        println("$TAG+token_${JSONRPC().getUserToken()}")
        //EventBus subscriber
        EventBus.getDefault().register(this@HomeActivity)
    }


    fun loadProfileData() {
        Profile().getProfile { status, result ->
            if (status) {
                try {
                    saveMyProfileInSharedPreference(result.toString())
                    var res = JSONObject(result.toString())
                    val jsonArray = res.getJSONArray("currencies")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.get(i) as JSONObject
                        val currency = Currency(jsonObject.getString("name"), jsonObject.getInt("order"), jsonObject.getBoolean("isDefault"))
                        currencies.add(currency)
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "loadProfileData_$e")
                }
                saveCurrencyOrderInSharedPreference()
                doUI {
                    profileViewSetting()
                }
            }
        }
    }

    fun loadBalanceData() {
        Trading().getBalancesList {
            val gson = Gson()
            val json = gson.toJson(it)
            XinWalletApp.instance.applicationContext.setPref(R.string.PREF_CURRENCY_BALANCE, json)
            val balData = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_BALANCE, "")
            Log.i(TAG, "saveCurrencyBalanceInSharedPreference_$balData")
            defaultCurySetting()
        }
    }

    fun profileViewSetting() {
        val profileJson = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_MYPROFILE, "")
        var res = JSONObject(profileJson)
        userName.text = res.getString("name")
        if (res.getString("avatar").trim().isNotEmpty()) {
            Glide.with(this).load(res.getString("avatar"))
                    .apply(RequestOptions().centerCrop().circleCrop()).into(avatar)
        }
    }

    fun defaultCurySetting() {
        val orderList = getPREFCurrencyOrderList()
        orderList.filter { it.isDefault }
        val curName = orderList[0].name
        default_currency.text = curName
        default_balance.text = numberFormat.format(getPREFCurrencyBalance(curName))
    }

    private fun saveMyProfileInSharedPreference(res: String) {
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_MYPROFILE, res)
        val profileData = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_MYPROFILE, "")
        Log.i(TAG, "saveMyProfileInSharedPreference_$profileData")

    }

    private fun saveCurrencyOrderInSharedPreference() {
        val json = gson.toJson(currencies)
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_CURRENCY_ORDER, json)
        val orderData = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_ORDER, "")
        Log.i(TAG, "saveCurrencyOrderInSharedPreference_$orderData")
    }

    fun balanceBtnClick(view: View) {
        var intent = Intent()
        intent.setClass(this, BalanceListActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    fun friendsActivity() {
        var intent = Intent()
        intent.setClass(this, ContactsActivity::class.java)
        startActivity(intent)
    }

    fun dealHistoryBtnOnClick(view: View) {
        val intent = Intent(this, HistoricalTxActivity::class.java)
        intent.putExtra("from", "Home")
        intent.putExtra("txFilter", "")
        startActivity(intent)
    }

    fun saveBtnOnClick(view: View) {}
    fun dealBtnOnClick(view: View) {}
    fun exchangeBtnOnClick(view: View) {}

    fun settingBtnOnClick(view: View) {
        val intent = Intent(this@HomeActivity, SettingActivity::class.java)
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: DataUpdateEvent) {
        when (event.type) {
            1 -> profileViewSetting()
            2 -> defaultCurySetting()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        when (requestCode) {
            123//相册
            -> {
                val dataUri = data.data
//                val cr = this.contentResolver
//                var bitmap = BitmapFactory.decodeStream(cr.openInputStream(dataUri))
                FuncApp().uploadAvatar(File(UriUtil().getPath(dataUri))) {
                    if (it) {

                        doUI {
                            // getProfileData()
                            Toast.makeText(this, R.string.Home_changedAvatar, Toast.LENGTH_SHORT).show()
                            loader.dismiss()
                        }
                    }

                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@HomeActivity)
    }

}
