package com.xinwang.xinwallet.presenter.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.R.id.ccp
import com.xinwang.xinwallet.R.id.etPhoneNumber
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.jsonrpc.FuncApp
import com.xinwang.xinwallet.presenter.activities.AgreementActivity
import com.xinwang.xinwallet.presenter.activities.PrivacyActivity
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name
    private var curPhoneNo: Phonenumber.PhoneNumber? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        privacyLink()
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
                //判別號碼是否正確
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
        FuncApp().reqSmsVerify(phoneNo) { status, msg ->
            runOnUiThread {
                loader.dismiss()
                if (status) {
                    showSoftInput(false, etPhoneNumber) // don't show soft input again, to avoid odd layout on next activity
                    val intent = Intent(this, SmsVerifyActivity::class.java)
                    intent.putExtra("countrycode", curPhoneNo!!.countryCode.toString())
                    intent.putExtra("phonenumber", curPhoneNo!!.nationalNumber.toString())
                    startActivity(intent)
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                }else{
                    Log.i(TAG,"smsError$msg")
                    SpringAnimator.failShakeAnimation(this, etPhoneNumber)
                    showSoftInput(true, etPhoneNumber)
                   // Toast.makeText(this,msg.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun privacyLink() {

        tvDesc.setText(Html.fromHtml(getString(R.string.Login_desc)))
        val sp = Html.fromHtml(getString(R.string.Login_desc))
        tvDesc.text = sp
        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        setTextHyperLinkListener(tvDesc, sp)
    }

    private fun setTextHyperLinkListener(textView: TextView, sp: Spanned) {
        val text = textView.text
        if (text is Spannable) {
            val end = text.length
            val urls = sp.getSpans(0, end, URLSpan::class.java)
            val style = SpannableStringBuilder(text)
            style.clearSpans()
            for (url in urls) {
                style.setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View?) {
                        var intent = Intent()

                        if (url.url.equals("agree")) {
                            intent.setClass(this@LoginActivity, AgreementActivity::class.java)
                        } else if (url.url.equals("privacy")) {
                            intent.setClass(this@LoginActivity, PrivacyActivity::class.java)
                        }
                        startActivity(intent)
                    }
                }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


            }
            textView.text = style
        }
    }
}




