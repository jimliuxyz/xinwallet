package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Contacts
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_add_friend.*

class AddFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
    }

    fun searchUser(view: View) {
        if (!number.text.isNullOrEmpty()) {
            Contacts().finedUserByPhone(arrayOf(number.text.trim().toString())) { status, result ->
                if (status && result!!.size >= 1) {
                    doUI {
                        resultView.visibility = View.VISIBLE
                        resultText.text = result!![0].name
                        resultID.text = result[0].userId
                        Glide.with(this).load(result!![0].avatar)
                                .apply(RequestOptions().centerCrop().circleCrop()).into(resultImg)
                    }
                } else {
                    resultView.visibility = View.GONE
                }
            }
        } else {
            resultView.visibility = View.GONE
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show()
        }
    }


    fun addFriends(view: View) {
        // Toast.makeText(this, "have added.", Toast.LENGTH_SHORT).show()

        Contacts().addFriends(arrayListOf(resultID.text.toString())) {
            if (it!!) {
                doUI {
                    Toast.makeText(this, "have added.", Toast.LENGTH_SHORT).show()
                }
                finish()
            }

        }

    }
}