package com.xinwang.xinwallet.presenter.activities.bank

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_transfer.*

class TransferActivity : XinActivity() {

    var selectedContact: Contacts? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)
        titleSetting()
        radioGroupSetting()
    }

    private fun titleSetting() {
        val backText = includeTransfer.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeTransfer.findViewById(R.id.title_name) as TextView
        val rightText = includeTransfer.findViewById(R.id.titleBarRightText) as TextView

        backText.text = getText(R.string.app_name)
        backText.setOnClickListener {
            finish()
        }
        titleBarText.text = getText(R.string.Receipt_Pay)
        rightText.visibility = View.GONE

        phoneBlock.visibility = View.GONE
    }

    private fun radioGroupSetting() {
        RGobjType.setOnCheckedChangeListener { radioGroup, selectedId ->
            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                    RGObjChanged(view.tag.toString())

                } else {
                    view.setTextColor(getColor(R.color.gray))
                }
            }
        }

        RGCuryType.setOnCheckedChangeListener { radioGroup, i ->

            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                } else {
                    view.setTextColor(getColor(R.color.gray))
                }
            }
        }

        countryCode.setOnCountryChangeListener {
            showContactData()
        }

        phoeNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                showContactData()
            }

        })

    }

    private fun RGObjChanged(action: String) {

        when (action) {
            "store" -> {
                phoneBlock.visibility = View.GONE
            }
            "phone" -> {
                phoneBlock.visibility = View.VISIBLE
            }
            getString(R.string.QRCode) -> {
                phoneBlock.visibility = View.GONE

            }
            "contacts" -> {
                phoneBlock.visibility = View.VISIBLE
            }
        }

    }


    private fun showContactData() {
        searchUserByPhone(countryCode.selectedCountryNameCode, phoeNo.text.toString()) { isGetData: Boolean?, contacts: Contacts? ->


            doUI {
                selectedContact = contacts
                if (isGetData!!) {
                    tvName.text = selectedContact!!.name
                    Glide.with(this).load(selectedContact!!.avatar)
                            .apply(RequestOptions().centerCrop().circleCrop())
                            .into(imgAvatar)
                } else {
                    tvName.text = ""
                    Glide.with(this).load(R.color.white)
                            .apply(RequestOptions().centerCrop().circleCrop())
                            .into(imgAvatar)

                }
            }

        }


    }
}