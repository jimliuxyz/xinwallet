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

open class XinActivity : AppCompatActivity() {

    companion object {
        private var LOCKTIME = 20 * 1000
        private var pauseTime = 0L
        private var lastAct: Any? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        if (this is IntroActivity)
            return
        if (this is SetPinCode1Activity || this is SetPinCode2Activity || this is LoginActivity || this is SmsVerifyActivity)
            return

        val isLoginReady = XinWalletService.instance.isLoginReady()
        if (!isLoginReady) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val needLock = System.currentTimeMillis() - pauseTime > LOCKTIME

        if (this is UnlockAppActivity)
            return

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
        super.onPause()

        if (this == lastAct) {
            lastAct = null
            pauseTime = System.currentTimeMillis()
        }
    }


    private var backPressTime = 0L
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

    fun exitApp(){
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
