package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_save_withdraw.*

class SaveWithdrawActivity : XinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_withdraw)
        titleSetting()
    }

    private fun titleSetting() {
        val backText = includeDepositWithdraw.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeDepositWithdraw.findViewById(R.id.title_name) as TextView
        val rightText = includeDepositWithdraw.findViewById(R.id.titleBarRightText) as TextView

        backText.text = getText(R.string.Cancel)
        backText.setOnClickListener {
            finish()
        }
        titleBarText.text = getText(R.string.Save_withdraw)
        rightText.visibility = View.GONE
    }
}
