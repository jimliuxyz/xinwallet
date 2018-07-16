package com.xinwang.xinwallet.presenter.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import kotlinx.android.synthetic.main.activity_login.*
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.tools.animation.SpringAnimator


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
            showSoftInput(true, etPhoneNumber)
        }
        ccp.showNameCode(false)
    }

    override fun onStart() {
        super.onStart()
        ccp.setCountryForNameCode("TW")
        etPhoneNumber.setText("")
//        etPhoneNumber.setText("0986123456")

        verifyPhoneNumber()
    }

    override fun onResume() {
        super.onResume()
        showSoftInput(true, etPhoneNumber)
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
        XinWalletService.instance.requestSMSVerify(phoneNo) { status, errmsg ->
            runOnUiThread {
                loader.dismiss()

                val ok = !status.isNullOrBlank() && status.equals("ok")

                if (ok) {
                    showSoftInput(false, etPhoneNumber) // don't show soft input again, to avoid odd layout on next activity

                    val intent = Intent(this, SmsVerifyActivity::class.java)
                    intent.putExtra("countrycode", curPhoneNo!!.countryCode.toString())
                    intent.putExtra("phonenumber", curPhoneNo!!.nationalNumber.toString())

                    startActivity(intent)
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                } else {
                    //todo: server may failed, show a message
                    if (!errmsg.isNullOrBlank()){
                        SpringAnimator.failShakeAnimation(this, etPhoneNumber)
                        Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}
