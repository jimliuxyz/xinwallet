package com.xinwang.xinwallet.presenter.activities.util

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.presenter.activities.*
import com.xinwang.xinwallet.presenter.activities.login.*
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import org.json.JSONObject
import java.util.ArrayList
import android.net.ConnectivityManager
import com.xinwang.xinwallet.jsonrpc.Contacts
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber


open class XinActivity : AppCompatActivity() {
    val TAG_XinActivity = "XinActivity"
    val RESULT_CODE = 9999

    companion object {
        private var LOCKTIME = 20 * 1000
        private var pauseTime = 0L
        private var lastAct: Any? = null
        private val gson = Gson()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //確認是否有網路
        if (!isNetworkConnect()) {
            Toast.makeText(this, "沒網路", Toast.LENGTH_LONG).show()
            exitApp() //關閉ＡＰＰ
        }
    }

    override fun onResume() {
        super.onResume()

        if (this is IntroActivity)
            return
        if (this is SetPinCode1Activity || this is SetPinCode2Activity || this is LoginActivity || this is SmsVerifyActivity)
            return

        //判斷是否已有token及pincode存在
        val isLoginReady = XinWalletService.instance.isLoginReady()
        if (!isLoginReady) {
            //token或pincode有缺，將頁面導回login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        //離開時間是否超過LOCKTIME
        val needLock = System.currentTimeMillis() - pauseTime > LOCKTIME

        if (this is UnlockAppActivity)
            return

        //離開時間過長，需要pinCode解碼
        if (needLock) {
            val intent = Intent(this, UnlockAppActivity::class.java)
//           intent.putExtra("backto", HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            return
        }
        lastAct = this

    }

    fun resetPauseTime() {
        pauseTime = System.currentTimeMillis()
    }

    override fun onPause() {
        //紀錄離開時間
        super.onPause()
        if (this == lastAct) {
            lastAct = null
            pauseTime = System.currentTimeMillis()
        }
    }

    private var backPressTime = 0L
    /*
    *按下實體手機返回鍵，2秒內即關閉ＡＰＰ
    * */
    override fun onBackPressed() {
        if (this is HomeActivity
                || this is LoginActivity
                || this is UnlockAppActivity
                || this is SetUsernameActivity
                || this is HistoricalTxActivity
                || this is BalanceListActivity
                || this is CurrencyHomePage) {
            if (System.currentTimeMillis() - backPressTime > 2000) {
                backPressTime = System.currentTimeMillis()
                Toast.makeText(this, "press again to exit!", Toast.LENGTH_SHORT).show()
                return
            }
            exitApp()
        } else
            super.onBackPressed()
    }

    fun exitApp() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
    }

    fun showSoftInput(visible: Boolean, view: View) {
        Handler().postDelayed({
            runOnUiThread {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

                if (visible)
                    imm?.showSoftInput(view, 0)
                else
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }, 100)
    }

    fun getCoinIconId(str: String): Int {
        when (str) {
            "BTC" -> return (R.drawable.ic_coin_btc)
            "USD" -> return (R.drawable.ic_coin_usd)
            "ETH" -> return (R.drawable.ic_coin_eth)
            else -> return (R.drawable.ic_coin_cny)
        }

    }

    fun getCurySymbol(str: String): Char {
        when (str) {//¥$Ξ₿
            "BTC" -> return '₿'
            "USD" -> return '$'
            "ETH" -> return 'Ξ'
            else -> return '¥'
        }

    }

    fun getCuryString(str: String): String {
        when (str) {
            "BTC" -> return getString(R.string.Currency_BTC)
            "USD" -> return getString(R.string.Currency_USD)
            "ETH" -> return getString(R.string.Currency_ETH)
            else -> return getString(R.string.Currency_CNY)
        }
    }


    fun getPREFCurrencyOrderList(): ArrayList<Currency> {
        val obj = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_ORDER, "")
        val type = object : TypeToken<ArrayList<Currency>>() {}.type
        return Gson().fromJson<ArrayList<Currency>>(obj, type)
    }


    fun getPREFCurrencyBalance(curName: String): Double {
        val obj = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_BALANCE, "")
        val type = object : TypeToken<ArrayList<Currency>>() {}.type
        val balanceList = Gson().fromJson<ArrayList<Currency>>(obj, type)
        return balanceList.filter { it.name == curName }[0].balance
    }


    fun updateCuryOrderFromServer(callback: (result: Boolean?) -> Unit) {
        Profile().getProfile { status: Boolean, it ->
            val jsonObject = JSONObject(it.toString())
            if (status && jsonObject.isNull("error")) {
                val currencies = jsonObject.getJSONArray("currencies").toString()
                val founderArray = gson.fromJson(currencies, Array<Currency>::class.java)
                val json = gson.toJson(founderArray)
                XinWalletApp.instance.applicationContext.setPref(R.string.PREF_CURRENCY_ORDER, json)
                val orderData = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_ORDER, "")
                Log.i(TAG_XinActivity, "updateCuryOrderFromServer_$orderData")
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun updateProfileFromServer(callback: (result: Boolean?) -> Unit) {
        Profile().getProfile { status, result ->
            if (status) {
                Log.i(TAG_XinActivity, "updatePrver_$result")
                XinWalletApp.instance.applicationContext.setPref(R.string.PREF_MYPROFILE, result)
                val profileData = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_MYPROFILE, "")
                Log.i(TAG_XinActivity, "updateProfileFromServer_$profileData")
                callback(true)

            } else {
                callback(false)
            }
        }
    }

    fun isNetworkConnect(): Boolean {
        //先取得此CONNECTIVITY_SERVICE
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //取得網路相關資訊
        val networkInfo = connManager.activeNetworkInfo
        //判斷是否有網路
        if (networkInfo == null || !networkInfo.isConnected) {
            Log.i("INFO", "沒網路")
            return false
        } else {
            Log.i("INFO", "有網路")
            return true
        }

    }


    fun searchUserByPhone(countryCode: String, phoneNo: String, callback: (result: Boolean?, com.xinwang.xinwallet.models.Contacts?) -> Unit) {

        var phoneUtil = PhoneNumberUtil.createInstance(this)
        var curPhoneNo: Phonenumber.PhoneNumber?
        try {
            curPhoneNo = phoneUtil.parse(phoneNo, countryCode)
                    .takeIf {
                        phoneUtil.isValidNumber(it)
                    }
            if (curPhoneNo != null) {
                val number = "${curPhoneNo.countryCode}${curPhoneNo.nationalNumber}"
                Contacts().finedUserByPhone(arrayOf(number)) { status, result ->
                    if (status && result!!.size >= 1) {
                        callback(true, result[0])
                    } else {
                        callback(false, null)
                    }

                }
            }
        } catch (e: NumberParseException) {
            callback(false, null)
            e.printStackTrace()
        }

        callback(false, null)
    }

}
