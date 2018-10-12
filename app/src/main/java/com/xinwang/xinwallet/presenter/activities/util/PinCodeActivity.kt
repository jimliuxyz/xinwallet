package com.xinwang.xinwallet.presenter.activities.util

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.login.LoginActivity
import com.xinwang.xinwallet.presenter.customviews.BRKeyboard
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import com.xinwang.xinwallet.tools.util.doIO
import kotlinx.android.synthetic.main.activity_pincode.*

open class PinCodeActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name

    private val CHAR_CLEAR: String = "–"
    private val CHAR_DONE: String = "●"

    private lateinit var etPin1: TextView
    private lateinit var etPin2: TextView
    private lateinit var etPin3: TextView
    private lateinit var etPin4: TextView
    private lateinit var etPin5: TextView
    private lateinit var etPin6: TextView
    private lateinit var pinLayout: View
    private lateinit var keypad: BRKeyboard

    private var pinDigits = 6
    private var pinCursor = 0
    private var pincode: CharArray = "      ".toCharArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pincode)

        showDigitsOption(false)

        pinLayout = findViewById(R.id.pinLayout)
        etPin1 = findViewById(R.id.pin1)
        etPin2 = findViewById(R.id.pin2)
        etPin3 = findViewById(R.id.pin3)
        etPin4 = findViewById(R.id.pin4)
        etPin5 = findViewById(R.id.pin5)
        etPin6 = findViewById(R.id.pin6)

        keypad = findViewById(R.id.brkeyboard)
        keypad.setShowDot(false)
        keypad.setBRButtonBackgroundResId(R.drawable.keyboard_trans_button)
        keypad.setBRButtonTextColor(R.color.gray)
//      keypad.setBreadground(getDrawable(R.drawable.bread_gradient))
        keypad.setBreadground(getDrawable(R.color.white))

        keypad.addOnInsertListener { key: String ->
            handleClick(key)
        }
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
        fillPinCode(pinCursor, dig.toString())
        pinCursor = if (pinCursor >= pinDigits) pinDigits else pinCursor + 1
    }

    private fun handleDeleteClick() {
        pinCursor = if (pinCursor <= 0) 0 else pinCursor - 1
        fillPinCode(pinCursor, CHAR_CLEAR)
    }

    private fun fillPinCode(idx: Int, str: String) {
        var replacer = if (str.equals(CHAR_CLEAR)) CHAR_CLEAR else CHAR_DONE

        when (idx) {
            1 - 1 -> etPin1.setText(replacer)
            2 - 1 -> etPin2.setText(replacer)
            3 - 1 -> etPin3.setText(replacer)
            4 - 1 -> etPin4.setText(replacer)
            5 - 1 -> etPin5.setText(replacer)
            6 - 1 -> etPin6.setText(replacer)
            else -> return
        }

        pincode[idx] = if (replacer.equals(CHAR_CLEAR)) ' ' else str[0]

        var pincodeStr = pincode.joinToString(separator = "").trim()
        if (pincodeStr.length == pinDigits) {
            doIO {
                runOnUiThread {
                    onPinCodeReady(pincodeStr)
                }
            }
        }
    }

    open fun sharkNClear() {
        SpringAnimator.failShakeAnimation(this, pinLayout)

        pinLayout.postOnAnimationDelayed({
            clearPinCode()
        }, 300)
    }

    //for override
    open fun onPinCodeReady(pincode: String) {
        sharkNClear()
        Toast.makeText(this, "you should override `onPincodeReady`", Toast.LENGTH_LONG).show()
    }

    open fun clearPinCode() {
        pinCursor = 0
        for (i in 0..6) {
            fillPinCode(i, CHAR_CLEAR)
        }
    }

    fun pinOptionClick(view: View) {
        togglePinDigits()
    }

    fun showDigitsOption(visible: Boolean) {
        btnPinOption.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun togglePinDigits(forceDigits: Int = 0) {
        clearPinCode()
        if ((forceDigits == 0 && pinDigits == 4) || forceDigits == 6) {
            pinDigits = 6
            etPin5.visibility = View.VISIBLE
            etPin6.visibility = View.VISIBLE
            btnPinOption.text = getString(R.string.PinCode_option2)
        } else if ((forceDigits == 0 && pinDigits == 6) || forceDigits == 4) {
            pinDigits = 4
            etPin5.visibility = View.GONE
            etPin6.visibility = View.GONE
            btnPinOption.text = getString(R.string.PinCode_option1)
        }

        pinLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
    }

    fun navBack(view: View) {
        backBtn()
    }

    open fun backBtn() {
        finish()
    }
}
