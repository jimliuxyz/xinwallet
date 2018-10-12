package com.xinwang.xinwallet.presenter.activities.login

import android.content.Intent
import android.os.Bundle
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.PinCodeActivity
import kotlinx.android.synthetic.main.activity_pincode.*

class SetPinCode1Activity : PinCodeActivity() {
    var notInitialSetting=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notInitialSetting = intent.getBooleanExtra("NotInitialSetting", false)
        showDigitsOption(false)

        brkeyboard.setBRKeyboardColor(R.color.white)
        tvTitle.text = getString(R.string.PinCode_Set1_title)
        tvDesc.text = getString(R.string.PinCode_Set1_desc)
    }

    override fun onStart() {
        super.onStart()
        clearPinCode()
    }

    override fun onPinCodeReady(pincode: String) {
        val intent = Intent(this, SetPinCode2Activity::class.java)
        intent.putExtra("pincode", pincode)
        if (notInitialSetting) {
            intent.putExtra("NotInitialSetting", true)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        finish()
    }

}
