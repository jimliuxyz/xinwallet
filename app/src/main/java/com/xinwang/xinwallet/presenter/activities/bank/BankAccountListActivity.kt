package com.xinwang.xinwallet.presenter.activities.bank

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.models.BankAccount
import com.xinwang.xinwallet.models.adapter.BankAccountAdapter
import com.xinwang.xinwallet.models.adapter.OnItemSelectedBtnListen
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import kotlinx.android.synthetic.main.activity_bank_account.*

class BankAccountListActivity : XinActivity() {

    var accountArray = arrayListOf("23940409280293", "389749823432")
    var accountBankArray = arrayListOf("中國銀行", "上海銀行")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_account)
        titleSetting()
        loadAccountList()
    }

    private fun titleSetting() {
        val backText = includeBankAccount.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeBankAccount.findViewById(R.id.title_name) as TextView?
        val rightText = includeBankAccount.findViewById(R.id.titleBarRightText) as TextView?

        backText!!.text = getString(R.string.Cancel)
        backText.setOnClickListener {
            finish()
        }
        titleBarText!!.text = getString(R.string.BankAccount)
        rightText!!.text = "+"
        rightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f)
        rightText.setTextColor(Color.BLUE)
        rightText.setOnClickListener {
            addBankAccount()
        }
    }

    private fun addBankAccount() {
    }

    private fun loadAccountList() {
        val bankList: ArrayList<BankAccount>? = arrayListOf(
                BankAccount("中國銀行上海銀行", "93-48758973239"),
                BankAccount("中央銀行", "9568499733-239"),
                BankAccount("上海銀行", "9348973239"),
                BankAccount("北京銀行", "3468739-39")
        )

        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_BANK_ACCOUNT, Gson().toJson(bankList))
        val array = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_BANK_ACCOUNT, "")
        val getBankList = Gson().fromJson(array, Array<BankAccount>::class.java).toCollection(ArrayList())

        if (getBankList.isNotEmpty()) {
            imgEmpty.visibility = View.GONE
            val adapter = BankAccountAdapter(getBankList, this)
            adapter.setOnSelectedBtnListen(object : OnItemSelectedBtnListen {
                override fun onSelectedClick(position: Int) {
                    val backIntent = Intent().putExtra("selectedAccount", position)
                    setResult(RESULT_CODE, backIntent)
                    finish()
                }
            })
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
        }

    }
}


