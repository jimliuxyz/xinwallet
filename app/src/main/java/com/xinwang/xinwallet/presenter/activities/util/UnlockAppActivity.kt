package com.xinwang.xinwallet.presenter.activities.util

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import kotlinx.android.synthetic.main.activity_pincoin.*


class UnlockAppActivity : PinCodeActivity() {

    private var backto: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backto = intent.getStringExtra("backto")

        tvTitle.text = getString(R.string.Unlock_title)
        tvDesc.text = getString(R.string.Unlock_desc)
        btnBack.visibility = View.GONE
        hline.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        clearPinCode()
    }

    override fun onPinCodeReady(pincode: String) {
        if (XinWalletService.instance.verifyPinCode(pincode)) {
            resetPauseTime()

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
}
