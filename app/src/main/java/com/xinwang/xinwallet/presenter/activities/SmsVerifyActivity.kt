package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.customviews.BRDialogView
import com.xinwang.xinwallet.presenter.customviews.BRKeyboard
import com.xinwang.xinwallet.tools.animation.BRDialog
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import kotlinx.android.synthetic.main.activity_sms_verify.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SmsVerifyActivity : XinActivity() {
    private val TAG = LoginActivity::class.java.name

    lateinit var etPincode: EditText
    lateinit var keypad: BRKeyboard
    var phonenumber =""
    var countrycode= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_verify)

        countrycode = intent.getStringExtra("countrycode")
        phonenumber = intent.getStringExtra("phonenumber")

        findViewById<TextView>(R.id.textDesc)?.let {
            it.text = "${it.text}\n+${countrycode} ${phonenumber}"
        }

        etPincode = findViewById(R.id.pincode)
//      etPincode.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        user_phone.setText("+"+countrycode.toString()+phonenumber.toString())
        keypad = findViewById(R.id.brkeyboard)
        keypad.setShowDot(false)
        keypad.setBRButtonBackgroundResId(R.drawable.keyboard_trans_button)
        keypad.setBRButtonTextColor(R.color.gray)
        keypad.setBreadground(getDrawable(R.drawable.bread_gradient))
        keypad.addOnInsertListener { key: String ->
            handleClick(key)
        }

       // etPincode.setText("123456")
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
        //SpringAnimator.failShakeAnimation(this, etPincode)
    }

    private fun handleDeleteClick() {
        var text = etPincode.text
        if (text.length > 0)
            etPincode.setText(text.substring(0, etPincode.text.length - 1))
    }

    fun nextClicked(view: View){


        if(etPincode.text.length==4){

            Thread {
                val client = OkHttpClient()
                val request = Request.Builder().url("https://twilio168.azurewebsites.net/api/HttpTriggerDBtest?code=hNVOk/a7GCnVlrTxBACnEQsapW0SHFeswnuCzfI84aJl8MkDzPk/rA==&phoneNo=${phonenumber.toString()}&smsCode=${etPincode.text}").build()
                val response = client.newCall(request).execute()
                val responseST=response.body()?.string()

              //  val ss=reader.readLine()
                val jsonObj = JSONObject(responseST)
                var result=  jsonObj.getString("status")
                if (result.equals("ok")){

                    BRDialog.showSimpleDialog(this, "", getString(R.string.SmsVerify_popup_verified))
//
//                    val intent = Intent(this, SmsVerifyActivity::class.java)
//                    intent.putExtra("countrycode", ccp.selectedCountryCode)
//                    intent.putExtra("phonenumber", etPhoneNumber.text.trim().toString())
//                    startActivity(intent)
                }else if(result.equals("ng")){


                }


                //runOnUiThread{  BRDialog.showSimpleDialog(this, result, getString(R.string.SmsVerify_popup_verified))}
            }.start()


        }else{

            Toast.makeText(this,R.string.Verificationcode_fourDigital,Toast.LENGTH_SHORT).show()
        }




//
//        BRDialog.showCustomDialog(this, "test", "test",
//                "ok", "close", object : BRDialogView.BROnClickListener {
//            override fun onClick(brDialogView: BRDialogView) {
//                brDialogView.dismissWithAnimation()
//            }
//        }, object : BRDialogView.BROnClickListener {
//            override fun onClick(brDialogView: BRDialogView) {
//                brDialogView.dismissWithAnimation()
//            }
//        }, null, 0)
//
//        BRDialog.showSimpleDialog(this, "", getString(R.string.SmsVerify_popup_verified))


    }

}
