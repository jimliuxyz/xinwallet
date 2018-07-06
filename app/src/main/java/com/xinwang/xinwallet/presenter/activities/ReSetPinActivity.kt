package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.customviews.BRKeyboard
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import kotlinx.android.synthetic.main.activity_set_pin.*
import android.R.array
import android.app.Activity
import android.content.Intent
import android.drm.DrmStore
import android.view.ActionMode
import com.xinwang.xinwallet.tools.animation.BRDialog
import java.io.File.separator
import java.util.*


class ReSetPinActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.name

    lateinit var etPin1: TextView
    lateinit var etPin2: TextView
    lateinit var etPin3: TextView
    lateinit var etPin4: TextView
    lateinit var etPin5: TextView
    lateinit var etPin6: TextView
    lateinit var pinLayout: View
    lateinit var keypad: BRKeyboard

    var pinDigits = 6
    var pinCursor = 0
    var pincode: CharArray = "      ".toCharArray()
    var firstPinCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_pin)

        textView.setText("Enter PinCode Again")

        val countrycode = intent.getStringExtra("countrycode")
        val phonenumber = intent.getStringExtra("phonenumber")

        firstPinCode = intent.getStringExtra("pinCode")

        findViewById<TextView>(R.id.textDesc)?.let {
            it.text = "${it.text}\n+${countrycode} ${phonenumber}"
        }

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
        keypad.setBreadground(getDrawable(R.drawable.bread_gradient))

        keypad.addOnInsertListener { key: String ->
            //            println(key)
            handleClick(key)
        }

//        findViewById<View>(R.id.btnNext).let {
//            it.visibility = View.GONE
//        }
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
        fillPinCode(pinCursor, "")
    }

    private fun fillPinCode(idx: Int, str: String) {

        when (idx) {
//            1 - 1 -> etPin1.setText(str)
//            2 - 1 -> etPin2.setText(str)
//            3 - 1 -> etPin3.setText(str)
//            4 - 1 -> etPin4.setText(str)
//            5 - 1 -> etPin5.setText(str)
//            6 - 1 -> etPin6.setText(str)


            1 - 1 -> etPin1.setText("*")
            2 - 1 -> etPin2.setText("*")
            3 - 1 -> etPin3.setText("*")
            4 - 1 -> etPin4.setText("*")
            5 - 1 -> etPin5.setText("*")
            6 - 1 -> etPin6.setText("*")

            else -> return
        }

        pincode[idx] = if (str.length > 0) str[0] else ' '

        if (pin6.text.length > 0) {

            if (pincode.joinToString(separator = "").equals(firstPinCode.trim())) {
                // save SharedPreferences
                var sharedPreferences = getSharedPreferences("shared1", Activity.MODE_PRIVATE)
                var editor = sharedPreferences.edit()
                editor.putString("UserPinCode", firstPinCode)
                if (editor.commit()) BRDialog.showSimpleDialog(this, "correct", getString(R.string.SmsVerify_popup_verified))
            } else {
                Toast.makeText(this, pincode.joinToString(separator = "") + "==" + firstPinCode.trim(), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SetPinActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                BRDialog.showSimpleDialog(this,"Incorrect","pin code is incorrect,")


            }

        }

    }

    private fun clearPinCOde() {
        pinCursor = 0
        for (i in 0..6) {
            fillPinCode(i, "")
        }
    }

    fun pinOptionClick(view: View) {
        togglePinDigits()
    }

    private fun togglePinDigits() {
        clearPinCOde()
        if (pinDigits == 4) {
            pinDigits = 6
            etPin5.visibility = View.VISIBLE
            etPin6.visibility = View.VISIBLE
        } else {
            pinDigits = 4
            etPin5.visibility = View.GONE
            etPin6.visibility = View.GONE
        }
        etPin5.invalidate()
        etPin6.invalidate()
    }

    fun nextClicked(view: View) {

        var sharedPreferences = getSharedPreferences("shared1", Activity.MODE_PRIVATE)
        Toast.makeText(this, sharedPreferences.getString("UserPinCode", ""), Toast.LENGTH_SHORT).show()
    }

}