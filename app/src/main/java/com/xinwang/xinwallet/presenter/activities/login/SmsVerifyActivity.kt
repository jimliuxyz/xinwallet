package com.xinwang.xinwallet.presenter.activities.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.jsonrpc.Auth
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import kotlinx.android.synthetic.main.activity_sms_verify.*
import java.util.*
import kotlin.concurrent.timerTask

class SmsVerifyActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name

    var phonenumber: String? = ""
    var countrycode: String? = ""
    var startuptime = System.currentTimeMillis()
    var smstime = 0L
    lateinit var timer: Timer
    var allowResend = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_verify)

        countrycode = intent.getStringExtra("countrycode")
        phonenumber = intent.getStringExtra("phonenumber")

        userPhoneNo.setText("+${countrycode} ${phonenumber}")

        //設定驗證碼欄位屬性
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
                //檢查驗證碼
                updatePasscode()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        etPasscode.setText("")

        etPasscode.hint = getString(R.string.SMS_enterOTP)
        etPasscode.setHintTextColor(Color.GRAY)
        updatePasscode()
    }

    override fun onResume() {
        super.onResume()
        showSoftInput(true, etPasscode)

        //每秒執行updateResend()
        timer = Timer("sms_resend_timer")
        timer.schedule(timerTask {
            runOnUiThread {
                updateResend()
            }
        }, 0, 1000L)
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    // 判斷是否可再發簡訊
    private fun updateResend() {

        //設定60秒後才能再發簡訊
        val timediff = (smstime + 60 * 1000) - System.currentTimeMillis()
        val waiting = timediff > 0
        if (allowResend) {
            btnResend.text = Html.fromHtml(getString(R.string.SMS_resend))
        } else {
            if (waiting) { //簡訊發送未超過60秒，不可再發送
                val countdown = Math.ceil(timediff.toDouble() / 1000).toInt()
                btnResend.text = getString(R.string.SMS_CountdownTimer, String.format("%2d", countdown))
            } else {
                allowResend = true
                updateResend()
            }
        }
    }

    private fun updatePasscode() {
        val passcode = etPasscode.text
        btnNext.isEnabled = passcode.length == 4
    }

    //驗證簡訊碼按鈕
    fun nextClicked(view: View) {

        if (etPasscode.text.length != 4)
            return

        val loader = LoaderDialogFragment()
        loader.show(supportFragmentManager, "LoaderDialogFragment")

        val phoneNo = "${countrycode}${phonenumber}"

        Auth().login(phoneNo, etPasscode.text.toString()) { res ->
            runOnUiThread {
                loader.dismiss()
                val ok = !res.isNullOrBlank() && !res.equals("null")
                if (ok) {
                    showSoftInput(false, etPasscode) // don't show soft input again, to avoid odd layout on next activity
                    val intent = Intent(this, SetPinCode1Activity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                } else {
                    SpringAnimator.failShakeAnimation(this, etPasscode)
                    etPasscode.postOnAnimationDelayed({
                        showSoftInput(true, etPasscode)
                        etPasscode.setText("")
                    }, 300)
                    // if (!errmsg.isNullOrBlank())
                    //     Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show()
                    etPasscode.hint = getString(R.string.SMS_Invalid)
                    etPasscode.setHintTextColor(Color.RED)
                }
            }
        }


    }

    fun navBack(view: View) {
        finish()
    }

    //重送簡訊
    fun resend(view: View) {
        if (!allowResend)
            return

        val phoneNo = "${countrycode}${phonenumber}"
        XinWalletService.instance.requestSMSVerify(phoneNo) { s: String?, s1: String? -> }

        smstime = XinWalletService.instance.getRequestSMSVerifyTime()
        allowResend = false
        updateResend()
    }

}
