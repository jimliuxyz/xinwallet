package com.xinwang.xinwallet.presenter.activities.util

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.presenter.activities.*
import kotlin.reflect.KClass

open class XinActivity : AppCompatActivity() {

    companion object {
        private var LOCKTIME = 10000
        private var pauseTime = System.currentTimeMillis()
        private var lastActClass = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        if (true)
            return
        println("onResume >>>> " + this)


        if (this is IntroActivity)
            return
        if (this is SetPinCode1Activity || this is SetPinCode2Activity || this is LoginActivity || this is SmsVerifyActivity)
            return

        val isLoginReady = XinWalletService.instance.isLoginReady()
        if (!isLoginReady){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val isLocked = System.currentTimeMillis() - pauseTime > LOCKTIME

        if (this is UnlockAppActivity)
            return

        println("curr >>>> " + this)
        if (isLocked) {
            val intent = Intent(this, UnlockAppActivity::class.java)
            intent.putExtra("backto", HomeActivity::class.java)
//            intent.putExtra("backto", "")

            startActivity(intent)
            return
        }
    }

    override fun onPause() {
        super.onPause()
        pauseTime = System.currentTimeMillis()
    }
}
