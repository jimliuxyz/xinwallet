package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.hbb20.CountryCodePicker
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.customviews.BRKeyboard
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import kotlinx.android.synthetic.main.activity_login.*
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.jimliuxyz.tsnote.services.translation.XinWalletService


class LoginActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name

    private var curPhoneNo: Phonenumber.PhoneNumber? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        etPasscode.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        //set keyboard style to number only
        etPhoneNumber.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        etPhoneNumber.transformationMethod = object : PasswordTransformationMethod() {
            override fun getTransformation(source: CharSequence, view: View): CharSequence {
                return source
            }
        }

        etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifyPhoneNumber()
            }
        })

        ccp.setOnCountryChangeListener {
            verifyPhoneNumber()
        }
        ccp.showNameCode(false)
    }

    override fun onStart() {
        super.onStart()
        ccp.setCountryForNameCode("TW")
        etPhoneNumber.setText("0986123456")
        etPhoneNumber.requestFocus()
        verifyPhoneNumber()
    }

    private fun verifyPhoneNumber(): Phonenumber.PhoneNumber? {
        var phoneUtil = PhoneNumberUtil.createInstance(this)

        curPhoneNo = null
        try {
            curPhoneNo = phoneUtil.parse(etPhoneNumber.text, ccp.selectedCountryNameCode)?.takeIf {
                phoneUtil.isValidNumber(it)
            }
        } catch (e: NumberParseException) {
            e.printStackTrace()
        }

        btnNext.isEnabled = curPhoneNo != null
        return curPhoneNo
    }

    fun loginClicked(view: View) {

        val loader = LoaderDialogFragment()
        loader.show(supportFragmentManager, "LoaderDialogFragment")

        val phoneNo = "${curPhoneNo!!.countryCode}${curPhoneNo!!.nationalNumber}"
        XinWalletService.instance.requestSMSVerify(phoneNo){status->
            runOnUiThread {
                loader.dismiss()

                val ok = !status.isNullOrBlank() && status.equals("ok")

                if (ok) {
                    val intent = Intent(this, SmsVerifyActivity::class.java)
                    intent.putExtra("countrycode", curPhoneNo!!.countryCode.toString())
                    intent.putExtra("phonenumber", curPhoneNo!!.nationalNumber.toString())

                    startActivity(intent)
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                } else {
                    //todo: server may failed, show a message
                }
            }
        }
    }

}
