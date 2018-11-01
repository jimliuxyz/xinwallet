package com.xinwang.xinwallet.presenter.activities.bank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.BankAccount
import com.xinwang.xinwallet.presenter.activities.CurrencySelectActivity
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.getPref
import kotlinx.android.synthetic.main.activity_save_withdraw.*
import org.json.JSONObject
import java.lang.Exception

class SaveWithdrawActivity : XinActivity() {
    val loader = LoaderDialogFragment()

    private val CURY1_REQCODE = 4893
    private val CURY2_REQCODE = 9384
    private val ACCOUNT1_REQCODE = 2343
    private val ACCOUNT2_REQCODE = 5943
    var cury1 = 0
    var cury2 = 0
    var account1 = 0
    var account2 = 0
    var bankList: ArrayList<BankAccount>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_withdraw)
        titleSetting()
    }

    fun bankAction(view: View) {
        when (view.id) {
            R.id.btnDeposit -> {
                try {
                    if (etDepositAmount.text.toString().toDouble() > 0) {
                        loader.show(supportFragmentManager, "LoaderDialogFragment")
                        doDeposit()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "cury Vaild", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btnWithdraw -> {
                try {
                    if (etWithdrawAmount.text.toString().toDouble() > 0) {
                        loader.show(supportFragmentManager, "LoaderDialogFragment")
                        doWithdraw()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "cury Vaild", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun doWithdraw() {
        Trading().withdraw(getPREFCurrencyOrderList()[cury2].name,
                etWithdrawAmount.text.toString().toDouble()) { status, result ->
            if (status) {
                doUI {
                    var jsonObject: JSONObject? = JSONObject(result)
                    loader.dismiss()
                    Toast.makeText(this,
                            "${jsonObject!!.get("receiptId")}/${jsonObject.get("statusCode")}",
                            Toast.LENGTH_LONG).show()
                }
            } else {
                doUI {
                    loader.dismiss()
                }
            }

        }
    }

    private fun doDeposit() {
        Trading().deposit(getPREFCurrencyOrderList()[cury1].name,
                etDepositAmount.text.toString().toDouble()) { status, result ->
            if (status) {
                doUI {
                    var jsonObject: JSONObject? = JSONObject(result)
                    loader.dismiss()
                    Toast.makeText(this,
                            "${jsonObject!!.get("receiptId")}/${jsonObject.get("statusCode")}",
                            Toast.LENGTH_LONG).show()
                }
            } else {
                doUI {
                    loader.dismiss()
                }
            }

        }
    }

    fun btnListOnClick(view: View) {
        when (view.id) {
            R.id.tvbankAccount1 -> {
                val bankIntent = Intent(this, BankAccountListActivity::class.java)
                bankIntent.putExtra("selectedAccount", account1)
                startActivityForResult(bankIntent, ACCOUNT1_REQCODE)
            }
            R.id.tvbankAccount2 -> {
                val bankIntent = Intent(this, BankAccountListActivity::class.java)
                bankIntent.putExtra("selectedAccount", account2)
                startActivityForResult(bankIntent, ACCOUNT2_REQCODE)

            }
            R.id.tvCury1 -> {
                val curyIntent = Intent(this, CurrencySelectActivity::class.java)
                curyIntent.putExtra("selectedCury", cury1)
                startActivityForResult(curyIntent, CURY1_REQCODE)
            }
            R.id.tvCury2 -> {
                val curyIntent = Intent(this, CurrencySelectActivity::class.java)
                curyIntent.putExtra("selectedCury", cury2)
                startActivityForResult(curyIntent, CURY2_REQCODE)
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_CODE) {
            when (requestCode) {
                ACCOUNT1_REQCODE -> {
                    val array = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_BANK_ACCOUNT, "")
                    bankList = Gson().fromJson(array, Array<BankAccount>::class.java).toCollection(ArrayList())
                    account1 = data!!.getIntExtra("selectedAccount", 0)
                    tvbankAccount1.text = "${bankList!![account1].backName}\n${bankList!![account1].accountNo}"
                }
                ACCOUNT2_REQCODE -> {
                    val array = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_BANK_ACCOUNT, "")
                    bankList = Gson().fromJson(array, Array<BankAccount>::class.java).toCollection(ArrayList())
                    account2 = data!!.getIntExtra("selectedAccount", 0)
                    tvbankAccount2.text = "${bankList!![account2].backName}\n${bankList!![account2].accountNo}"

                }
                CURY1_REQCODE -> {
                    cury1 = data!!.getIntExtra("selectedCury", 0)
                    val enCury = getPREFCurrencyOrderList()[cury1].name
                    tvCury1.text = getCuryString(enCury)
                }
                CURY2_REQCODE -> {
                    cury2 = data!!.getIntExtra("selectedCury", 0)
                    val enCury = getPREFCurrencyOrderList()[cury2].name
                    tvCury2.text = getCuryString(enCury)
                }
            }
        }

    }
}
