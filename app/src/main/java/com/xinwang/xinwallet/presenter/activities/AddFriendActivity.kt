package com.xinwang.xinwallet.presenter.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.text.InputType
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Contacts
import com.xinwang.xinwallet.tools.util.doUI
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import kotlinx.android.synthetic.main.activity_add_friend.*
import android.view.inputmethod.InputMethodManager
import com.xinwang.xinwallet.presenter.activities.util.XinActivity


class AddFriendActivity : XinActivity() {

    private var friendId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        titleSetting()
        countryCodePicker.setCountryForNameCode("TW")
        setEditTextListener()
    }

    private fun setEditTextListener() {
        //set keyboard style to number only
        number.inputType = InputType.TYPE_CLASS_NUMBER
        number.queryHint = getString(R.string.Login_title)
        number.setIconifiedByDefault(false)
        number.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                searchUser()
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
        })
        countryCodePicker.setOnCountryChangeListener {
            searchUser()
        }
    }

    fun searchUser() {
        //隱藏result
        resultView.visibility = View.GONE
        var phoneUtil = PhoneNumberUtil.createInstance(this)
        var curPhoneNo: Phonenumber.PhoneNumber?
        try {
            curPhoneNo = phoneUtil.parse(number.query, countryCodePicker.selectedCountryNameCode)
                    .takeIf {
                        phoneUtil.isValidNumber(it)
                    }
            if (curPhoneNo != null) {
                val phoneNo = "${curPhoneNo.countryCode}${curPhoneNo.nationalNumber}"
                Contacts().finedUserByPhone(arrayOf(phoneNo)) { status, result ->
                    if (status && result!!.size >= 1) {
                        doUI {
                            //closeKeyboard
                            val view = window.peekDecorView()
                            if (view != null) {
                                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                            }
                            resultView.visibility = View.VISIBLE
                            resultText.text = result[0].name
                            friendId = result[0].userId
                            Glide.with(this).load(result[0].avatar)
                                    .apply(RequestOptions().centerCrop().circleCrop()).into(resultImg)
                        }
                    }
                }
            }
        } catch (e: NumberParseException) {
            e.printStackTrace()
        }

    }

    private fun titleSetting() {
        val backText = includeAddFriend.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeAddFriend.findViewById(R.id.title_name) as TextView
        val rightText = includeAddFriend.findViewById(R.id.titleBarRightText) as TextView

        backText.text = getString(R.string.Contacts)
        backText.setOnClickListener {
            finish()
        }
        rightText.visibility = View.GONE
        titleBarText.text = getString(R.string.AddContacts)
    }

    fun addFriends(view: View) {
        Contacts().addFriends(arrayListOf(friendId!!)) {
            if (it!!) {
                finish()
            }

        }
    }
}