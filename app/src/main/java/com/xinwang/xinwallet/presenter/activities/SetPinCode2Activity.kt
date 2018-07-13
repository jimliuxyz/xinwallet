package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_pincoin.*

class SetPinCode2Activity : PinCodeActivity() {

    lateinit var pincode1: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pincode1 = intent.getStringExtra("pincode")
    }

    override fun onPinCodeReady(pincode: String) {
        if (pincode1.equals(pincode)) {
            XinWalletService.instance.setPinCode(pincode)
            val intent = Intent(this, UnlockAppActivity::class.java)
            intent.putExtra("backto", HomeActivity::class.java.canonicalName)

            startActivity(intent)
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        } else {
            sharkNClear()
        }
    }
}
