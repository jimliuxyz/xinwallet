package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import com.hbb20.CountryCodePicker
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.customviews.BRKeyboard
import com.xinwang.xinwallet.tools.animation.SpringAnimator

class LoginActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name

    lateinit var etPhoneNumber: EditText
    lateinit var ccp: CountryCodePicker
    lateinit var keypad: BRKeyboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etPhoneNumber = findViewById(R.id.phonenumber)
//        etPincode.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        ccp = findViewById(R.id.ccp)
        ccp.setOnCountryChangeListener {
//            println(ccp.selectedCountryNameCode)
        }

        keypad = findViewById(R.id.brkeyboard)
        keypad.setShowDot(false)
        keypad.setBRButtonBackgroundResId(R.drawable.keyboard_trans_button)
        keypad.setBRButtonTextColor(R.color.gray)
        keypad.setBreadground(getDrawable(R.drawable.bread_gradient))

        keypad.addOnInsertListener { key: String ->
//            println(key)
            handleClick(key)
        }

        etPhoneNumber.setText("1234567890")
    }

    private fun handleClick(key: String?) {

        if (key == null) {
            Log.e(TAG, "handleClick: key is null! ")
            return
        }

        if (key.isEmpty()) {
            handleDeleteClick()
        } else if (Character.isDigit(key[0])) {
            handleDigitClick(Integer.parseInt(key.substring(0, 1)))
        } else {
            Log.e(TAG, "handleClick: oops: $key")
        }
    }

    private fun handleDigitClick(dig: Int?) {
        etPhoneNumber.append(dig.toString())
        SpringAnimator.failShakeAnimation(this, etPhoneNumber)
    }

    private fun handleDeleteClick() {
        var text = etPhoneNumber.text
        if (text.length > 0)
            etPhoneNumber.setText(text.substring(0, etPhoneNumber.text.length - 1))
    }

    fun loginClicked(view: View){
        val intent = Intent(this, SmsVerifyActivity::class.java)
        intent.putExtra("countrycode", ccp.selectedCountryCode)
        intent.putExtra("phonenumber", etPhoneNumber.text.trim().toString())
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

}
