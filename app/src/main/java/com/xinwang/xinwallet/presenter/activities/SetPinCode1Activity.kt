package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_pincoin.*

class SetPinCode1Activity : PinCodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brkeyboard.setBRKeyboardColor(R.color.white)
    }

    override fun onStart() {
        super.onStart()
        clearPinCode()
    }

    override fun onPinCodeReady(pincode: String) {
//        Toast.makeText(this, pincode, Toast.LENGTH_LONG).show()

        val intent = Intent(this, SetPinCode2Activity::class.java)
        intent.putExtra("pincode", pincode)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }
}
