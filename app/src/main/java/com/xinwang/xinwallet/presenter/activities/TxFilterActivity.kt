package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.models.adapter.ContactsHorizontalAdapter
import com.xinwang.xinwallet.models.adapter.IOnBtnClickListen
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.DatePickerActivity
import kotlinx.android.synthetic.main.activity_date_picker.*
import kotlinx.android.synthetic.main.activity_tx_filter.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class TxFilterActivity : XinActivity() {

    private var customDate1: Long = 0
    private var customDate2: Long = 0
    private val RESULTCODE_CUSTOMDATE = 23
    private val RESULTCODE_SELECTEDTARGET = 45

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tx_filter)
        settingTitleBar()
        radioGroupSetting()
        recycleViewSetting()
        //預設按鈕
        TxActionDefault.performClick()
        TxCurrencyDefault.performClick()
        TxDateDefault.performClick()
    }

    private fun recycleViewSetting() {
        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val data: ArrayList<Contacts> = ArrayList()
        recyclerView1.adapter = ContactsHorizontalAdapter(data, this, false)

    }

    fun addTxTargetOnClick(view: View) {
        val intent = Intent(this, ContactsCheckBoxActivity::class.java)
        startActivityForResult(intent, RESULTCODE_SELECTEDTARGET)
    }

    private fun settingTitleBar() {
        val backText = includeTitlebarTxFilter.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeTitlebarTxFilter.findViewById(R.id.title_name) as TextView?
        val rightText = includeTitlebarTxFilter.findViewById(R.id.titleBarRightText) as TextView?
        backText?.text = getString(R.string.Historical_Transaction)
        titleBarText?.text = getString(R.string.HistoricalTx_Filter)
        rightText?.text = getString(R.string.Btn_done)
        rightText?.setOnClickListener {
            filterDone()
        }
        backText!!.setOnClickListener {
            finish()
        }
    }

    private fun filterDone() {
        try {
            val TxView: RadioButton = findViewById(RGTxType.checkedRadioButtonId)
            val dateView: RadioButton = findViewById(RGDateType.checkedRadioButtonId)
            val currencyView: RadioButton = findViewById(RGCurrencyType.checkedRadioButtonId)
            val sortingView: RadioButton = findViewById(RGOrder.checkedRadioButtonId)
            finish()
            Toast.makeText(this, sortingView.getTag().toString() + "/" +
                    TxView.getTag().toString() + "/"
                    + dateView.getTag().toString() + "/"
                    + currencyView.getTag().toString(),
                    Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
        }
    }

    private fun radioGroupSetting() {
        RGTxType.setOnCheckedChangeListener { radioGroup, selectedID ->
            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                } else {
                    view.setTextColor(getColor(R.color.gray))
                }

            }
        }

        RGDateType.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                } else {
                    view.setTextColor(getColor(R.color.gray))
                }
            }

        }

        RGCurrencyType.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            for (i in 1..radioGroup.childCount) {
                val view = radioGroup.getChildAt(i - 1) as RadioButton
                if (view.isChecked) {
                    view.setTextColor(getColor(R.color.wallet_orange))
                } else {
                    view.setTextColor(getColor(R.color.gray))
                }
            }
        }

    }

    fun customDateOnClick(view: View) {
        val intent = Intent(this@TxFilterActivity, DatePickerActivity::class.java)
        startActivityForResult(intent, RESULTCODE_CUSTOMDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RESULTCODE_CUSTOMDATE -> {
                try {
                    customDate1 = data!!.getStringExtra("date1").toLong()
                    customDate2 = data.getStringExtra("date2").toLong()
                    val sdf = SimpleDateFormat("MMM,dd")
                    val date1 = Date(customDate1)
                    val date2 = Date(customDate2)
                    customDate.text = sdf.format(date1) + "-" + sdf.format(date2)
                } catch (e: Exception) {
                    println(e)
                    TxDateDefault.performClick()
                }
            }
            RESULTCODE_SELECTEDTARGET -> {
                try {
                    val getData = data!!.getStringExtra("selectedTarget")

                    val type = object : TypeToken<java.util.ArrayList<Contacts>>() {}.type
                    val selectedContacts = Gson().fromJson<java.util.ArrayList<Contacts>>(getData, type)
                    if (selectedContacts.size > 0) {
                        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        var horizontalAdapter = ContactsHorizontalAdapter(selectedContacts, this, false)
                        horizontalAdapter.setOnBtnClickListen(object : IOnBtnClickListen {
                            override fun onClickListen(position: Int) {
                            }
                        })
                        recyclerView1.adapter = horizontalAdapter
                        textViewWho.text = ""
                    } else {
                        textViewWho.text = getString(R.string.TxSort_All)
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
    }
}