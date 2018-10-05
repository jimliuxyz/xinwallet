package com.xinwang.xinwallet.presenter.activities.util

import android.app.ActivityManager
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
import android.content.ComponentName
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.jsonrpc.Auth
import com.xinwang.xinwallet.jsonrpc.JSONRPC
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.models.TransactionRecord
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import org.json.JSONObject
import java.util.ArrayList


//class A : XinActivity(){
//    override fun customOnEvent(){
////        super.onEvent(E)
//    }
//}

open class XinActivity : AppCompatActivity() {

    companion object {
        private var LOCKTIME = 20 * 1000
        private var pauseTime = 0L
        private var lastAct: Any? = null
        private val gson = Gson()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


//    final fun onEvent(E){
//
//    }
//
//    fun customOnEvent(E){
//
//    }

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

    fun getPREFCurrencyOrderList(): ArrayList<Currency> {
        val obj = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_ORDER, "")
        val type = object : TypeToken<ArrayList<Currency>>() {}.type
        return Gson().fromJson<ArrayList<Currency>>(obj, type)
    }

    fun getPREFCurrencyBalance(curName: String): Double {
        val obj = XinWalletApp.instance.applicationContext.getPref(R.string.REF_CURRENCY_BALANCE, "")
        val type = object : TypeToken<ArrayList<Currency>>() {}.type
        val balanceList = Gson().fromJson<ArrayList<Currency>>(obj, type)
        return balanceList.filter { it.name == curName }[0].balance

    }


    fun saveCurrencyOrderInSharedPreference1() {
        Profile().getProfile { status: Boolean, it ->
            val jsonObject = JSONObject(it.toString())
            if (status && jsonObject.isNull("error")) {
                val currencies = jsonObject.getJSONArray("currencies").toString()
                //  val jsonArrayString = JSONObject(result).getJSONArray("currencies").toString()
                val founderArray = gson.fromJson(currencies, Array<Currency>::class.java)
                val json = gson.toJson(founderArray)
                XinWalletApp.instance.applicationContext.setPref(R.string.PREF_CURRENCY_ORDER, json)
                val orderData = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_CURRENCY_ORDER, "")
                Log.i(this.toString(), "saveCurrencyOrderInSharedPreference1_$orderData")
            } else {
                Toast.makeText(this, "尚未更新幣別排序", Toast.LENGTH_SHORT).show()
            }
        }


    }

}
