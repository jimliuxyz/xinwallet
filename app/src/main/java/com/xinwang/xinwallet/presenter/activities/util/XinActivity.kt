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
import com.xinwang.xinwallet.jsonrpc.Auth
import com.xinwang.xinwallet.jsonrpc.JSONRPC


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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isTokenAvailable()
    }

    fun isTokenAvailable() {
        if (this is SetPinCode1Activity || this is SetPinCode2Activity
                || this is LoginActivity || this is SmsVerifyActivity || this is IntroActivity)
            return

        Auth().isToknAvailable { status, result ->
            if (!status) {
                JSONRPC().delUserToken()
            }
        }
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        println("cls:" + cn.className)
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
        //現在離開減上次離開時間是否超過LOCKTIME
        val needLock = System.currentTimeMillis() - pauseTime > LOCKTIME

        if (this is UnlockAppActivity)
            return

        //離開時間過長，需要pinCode解碼
        if (needLock) {
            val intent = Intent(this, UnlockAppActivity::class.java)
//            intent.putExtra("backto", HomeActivity::class.java)
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
                || this is SetUsernameActivity) {
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

}
