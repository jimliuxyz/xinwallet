package com.xinwang.xinwallet.presenter.activities.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.jsonrpc.Auth
import com.xinwang.xinwallet.jsonrpc.JSONRPC
import com.xinwang.xinwallet.presenter.activities.IntroActivity
import com.xinwang.xinwallet.presenter.activities.login.LoginActivity
import com.xinwang.xinwallet.presenter.activities.login.SetPinCode1Activity
import com.xinwang.xinwallet.presenter.activities.login.SetPinCode2Activity
import com.xinwang.xinwallet.presenter.activities.login.SmsVerifyActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_pincoin.*


class UnlockAppActivity : PinCodeActivity() {
    var loader = LoaderDialogFragment()
    private var backto: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backto = intent.getStringExtra("backto")
        tvTitle.text = getString(R.string.Unlock_title)
        tvDesc.text = getString(R.string.Unlock_desc)
        btnBack.visibility = View.GONE
        hline.visibility = View.GONE

        val digits = XinWalletService.instance.getPinCodeDigits()
        showDigitsOption(false)
        togglePinDigits(digits)
    }

    override fun onStart() {
        super.onStart()
        clearPinCode()
    }

    fun onPinCodeReady1(pincode: String) {
        if (XinWalletService.instance.verifyPinCode(pincode)) {
            resetPauseTime()
            //導回上次離開的頁面
            if (!backto.isNullOrBlank()) {
                val intent = Intent()
                intent.setClassName(this, backto)
                startActivity(intent)
            }
            finish()
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        } else {
            sharkNClear()
        }
    }


    override fun onPinCodeReady(pincode: String) {
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        if (XinWalletService.instance.verifyPinCode(pincode)) {
            val intent = Intent()
            Auth().isToknAvailable { status, result ->
                loader.dismiss()
                if (!status) {
                    JSONRPC().delUserToken()
                    doUI {
                        Toast.makeText(this, R.string.Unlock_tokenUnavailable, Toast.LENGTH_SHORT).show()
                    }
                    intent.setClass(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    resetPauseTime()
                    finish()
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                }
            }
        } else {
            loader.dismiss()
            sharkNClear()
        }
    }


    fun isTokenAvailable() {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        println("cls:" + cn.className)
    }

}
