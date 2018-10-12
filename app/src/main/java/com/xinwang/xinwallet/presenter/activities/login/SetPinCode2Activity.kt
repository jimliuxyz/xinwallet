package com.xinwang.xinwallet.presenter.activities.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.presenter.activities.HomeActivity
import com.xinwang.xinwallet.presenter.activities.util.PinCodeActivity
import kotlinx.android.synthetic.main.activity_pincode.*

class SetPinCode2Activity : PinCodeActivity() {

    lateinit var pincode1: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pincode1 = intent.getStringExtra("pincode")

        showDigitsOption(false)
        togglePinDigits(pincode1.length)

        brkeyboard.setBRKeyboardColor(R.color.white)
        tvTitle.text = getString(R.string.PinCode_Set2_title)
    }

    override fun onPinCodeReady(pincode: String) {
        if (pincode1.equals(pincode)) {
            XinWalletService.instance.setPinCode(pincode)
            resetPauseTime()
            //非初始設定，finish
            if (intent.getBooleanExtra("NotInitialSetting", false)) {
                finish()
                return
            } else {
                val intent = Intent(this, SetUsernameActivity::class.java)
                intent.putExtra("backto", HomeActivity::class.java.canonicalName)
                startActivity(intent)
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)


            }
            //初始設定，轉到設定ID頁面
        } else {
            sharkNClear()
            tvInvalidHint.visibility = View.VISIBLE
        }
    }

    override fun backBtn() {
        val intent = Intent(this@SetPinCode2Activity, SetPinCode1Activity::class.java)
        startActivity(intent)
        finish()

    }


}
