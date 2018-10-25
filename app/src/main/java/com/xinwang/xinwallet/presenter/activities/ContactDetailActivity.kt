package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_contact_detail.*

class ContactDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)
        titleSetting()
        getContactData()
    }

    private fun getContactData() {
        Glide.with(this@ContactDetailActivity).load(intent.getStringExtra("avatar"))
                .apply(RequestOptions().centerCrop().circleCrop()).into(image)
        tvName.text=intent.getStringExtra("name")
        tvPhone.text=intent.getStringExtra("phone")

    }

    private fun titleSetting() {
        val backText = includeContactDetail.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeContactDetail.findViewById(R.id.title_name) as TextView
        val rightText = includeContactDetail.findViewById(R.id.titleBarRightText) as TextView
        backText.text=getText(R.string.Cancel)
        backText.setOnClickListener {
            finish()
        }
        titleBarText.text=getText(R.string.Contacts)
        rightText.visibility=View.GONE
    }
}
