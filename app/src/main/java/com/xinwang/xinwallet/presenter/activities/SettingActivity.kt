package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.presenter.activities.login.SetPinCode1Activity
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.getPref
import kotlinx.android.synthetic.main.activity_setting.*
import org.json.JSONObject
import java.util.*

class SettingActivity : XinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        titleSetting()
        getProfile()
    }

    private fun getProfile() {
        val profileJson = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_MYPROFILE, "")
        val orderList = getPREFCurrencyOrderList()
        orderList.filter {
            it.isDefault
        }
        tvDefaultCury.text = orderList[0].name
        var res = JSONObject(profileJson)
        tvProfileName.text = res.getString("name")
        if (res.getString("avatar").trim().isNotEmpty()) {
            Glide.with(this).load(res.getString("avatar"))
                    .apply(RequestOptions().centerCrop().circleCrop()).into(imageView)
        }
        tvLanguage.text = Locale.getDefault().displayLanguage
    }

    private fun titleSetting() {
        val backText = includeSetting.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeSetting.findViewById(R.id.title_name) as TextView
        val rightText = includeSetting.findViewById(R.id.titleBarRightText) as TextView
        titleBarText.text = getText(R.string.Setting)
        rightText.visibility = View.GONE
        backText.text = getText(R.string.app_name)
        backText.setOnClickListener {
            finish()
        }
    }

    fun myProfileOnClick(view: View) {

        val intent = Intent(this@SettingActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun changePinCodeOnClick(view:View){
        val intent=Intent(this,SetPinCode1Activity::class.java)
        intent.putExtra("NotInitialSetting",true)
        startActivity(intent)
    }

}
