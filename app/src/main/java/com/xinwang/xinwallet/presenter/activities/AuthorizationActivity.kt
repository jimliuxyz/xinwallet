package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.xinwang.xinwallet.R

class AuthorizationActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)
    }

    fun navBack(view: View) {
        finish()
    }
}
