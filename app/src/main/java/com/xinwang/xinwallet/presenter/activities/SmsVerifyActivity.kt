package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import kotlinx.android.synthetic.main.activity_sms_verify.*

class SmsVerifyActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name

    var phonenumber = ""
    var countrycode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_verify)

        countrycode = intent.getStringExtra("countrycode")
        phonenumber = intent.getStringExtra("phonenumber")

        userPhoneNo.setText("+${countrycode} ${phonenumber}")

        etPasscode.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        etPasscode.transformationMethod = object : PasswordTransformationMethod() {
            override fun getTransformation(source: CharSequence, view: View): CharSequence {
                return source
            }
        }

        etPasscode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatePasscode()
            }
        })

    }

    override fun onStart() {
        super.onStart()
//        etPasscode.setText("")
        etPasscode.setText("3333")
        updatePasscode()
    }

    override fun onResume() {
        super.onResume()
        showSoftInput(true, etPasscode)
    }

    private fun updatePasscode() {
        val passcode = etPasscode.text
        btnNext.isEnabled = passcode.length == 4
    }

    fun nextClicked(view: View) {

        if (etPasscode.text.length != 4)
            return

        val loader = LoaderDialogFragment()
        loader.show(supportFragmentManager, "LoaderDialogFragment")

        val phoneNo = "${countrycode}${phonenumber}"
        XinWalletService.instance.verifySMSPasscode(phoneNo, etPasscode.text.toString()) { status, errmsg ->
            runOnUiThread {
                loader.dismiss()

                val ok = !status.isNullOrBlank() && status.equals("ok")

                if (ok) {
                    showSoftInput(false, etPasscode) // don't show soft input again, to avoid odd layout on next activity

                    val intent = Intent(this, SetPinCode1Activity::class.java)

                    startActivity(intent)
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
//                    finish()
                } else {
                    SpringAnimator.failShakeAnimation(this, etPasscode)
                    etPasscode.postOnAnimationDelayed({
                        showSoftInput(true, etPasscode)
                        etPasscode.setText("")
                    }, 300)
                    if (!errmsg.isNullOrBlank())
                        Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun navBack(view: View) {
        finish()
    }

    fun resend(view: View) {
        //todo : resend
        super.onBackPressed()
    }

}
