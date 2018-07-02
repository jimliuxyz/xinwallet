package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.customviews.BRDialogView
import com.xinwang.xinwallet.presenter.customviews.BRKeyboard
import com.xinwang.xinwallet.tools.animation.BRDialog
import com.xinwang.xinwallet.tools.animation.SpringAnimator

class SmsVerifyActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name

    lateinit var etPincode: EditText
    lateinit var keypad: BRKeyboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_verify)

        val countrycode = intent.getStringExtra("countrycode")
        val phonenumber = intent.getStringExtra("phonenumber")

        findViewById<TextView>(R.id.textDesc)?.let {
            it.text = "${it.text}\n+${countrycode} ${phonenumber}"
        }

        etPincode = findViewById(R.id.pincode)
//        etPincode.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        keypad = findViewById(R.id.brkeyboard)
        keypad.setShowDot(false)
        keypad.setBRButtonBackgroundResId(R.drawable.keyboard_trans_button)
        keypad.setBRButtonTextColor(R.color.gray)
        keypad.setBreadground(getDrawable(R.drawable.bread_gradient))

        keypad.addOnInsertListener { key: String ->
            //            println(key)
            handleClick(key)
        }

        etPincode.setText("123456")
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
        etPincode.append(dig.toString())
        SpringAnimator.failShakeAnimation(this, etPincode)
    }

    private fun handleDeleteClick() {
        var text = etPincode.text
        if (text.length > 0)
            etPincode.setText(text.substring(0, etPincode.text.length - 1))
    }

    fun loginClicked(view: View){

        BRDialog.showCustomDialog(this, "test", "test",
                "ok", "close", object : BRDialogView.BROnClickListener {
            override fun onClick(brDialogView: BRDialogView) {
                brDialogView.dismissWithAnimation()
            }
        }, object : BRDialogView.BROnClickListener {
            override fun onClick(brDialogView: BRDialogView) {
                brDialogView.dismissWithAnimation()
            }
        }, null, 0)

    }

}
