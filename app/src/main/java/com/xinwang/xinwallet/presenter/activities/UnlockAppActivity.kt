package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.R


class UnlockAppActivity : PinCodeActivity() {

    private var backto: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideAppBar()
        backto = intent.getStringExtra("backto")
    }

    override fun onStart() {
        super.onStart()
        clearPinCode()
    }

    override fun onPinCodeReady(pincode: String) {
        if (XinWalletService.instance.verifyPinCode(pincode)) {
            println("go ~ " + backto)

            if (!backto.isNullOrBlank()) {
                val intent = Intent()
                intent.setClassName(this, backto)

                startActivity(intent)
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            } else {
                finish()
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            }
        } else {
            sharkNClear()
        }
    }

    private var backtime = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - backtime > 2000) {
            backtime = System.currentTimeMillis()
            Toast.makeText(this, "press again to exit!", Toast.LENGTH_SHORT).show()
            return
        }

        //send app to background
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
    }
}
