package com.xinwang.xinwallet.presenter.activities


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Excurrency
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_exchange.*
import org.json.JSONObject
import java.text.NumberFormat

class ExchangeActivity : XinActivity() {

    private val CURY1_REQCODE = 3489
    private val CURY2_REQCODE = 2394
    private var cury1 = 0
    private var cury2 = 1
    private var rate = 1.0

    val curyOrderList = getPREFCurrencyOrderList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)
        titleSetting()
        currencyChanged(CURY1_REQCODE)
        currencyChanged(CURY2_REQCODE)
        getRate()
        tvTotalAmount.text = getCurySymbol(curyOrderList[cury1].name) + "" + getPREFCurrencyBalance(curyOrderList[cury1].name)
    }

    private fun getRate() {
        Excurrency().getExRate { status, result ->
            if (status) {
                var jsonObject: JSONObject? = JSONObject(result)
                rate = jsonObject!!.get("USDCNY-buy") as Double
                doUI {
                    tvRate.text = jsonObject!!.get("USDCNY-buy").toString()
                    updateAmount()
                }
            }
        }
    }

    private fun titleSetting() {
        val backText = includeExchange.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeExchange.findViewById(R.id.title_name) as TextView
        val rightText = includeExchange.findViewById(R.id.titleBarRightText) as TextView

        backText.text = getText(R.string.app_name)
        backText.setOnClickListener {
            finish()
        }
        titleBarText.text = getText(R.string.Exchange)
        rightText.visibility = View.GONE

        //add TextChanged
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateAmount()
            }


        })
    }

    fun selectCury(view: View) {
        val intent = Intent(this@ExchangeActivity, CurrencySelectActivity::class.java)
        when (view.id) {
            R.id.curyEnName1, R.id.curyName1, R.id.imgCury1, R.id.imgDown1 -> {
                intent.putExtra("selectedCury", cury1)
                startActivityForResult(intent, CURY1_REQCODE)

            }
            R.id.curyEnName2, R.id.curyName2, R.id.imgCury2, R.id.imgDown2 -> {
                intent.putExtra("selectedCury", cury2)
                startActivityForResult(intent, CURY2_REQCODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_CODE) {
            when (requestCode) {
                CURY1_REQCODE -> {
                    cury1 = data!!.getIntExtra("selectedCury", 0)
                    currencyChanged(CURY1_REQCODE)
                }
                CURY2_REQCODE -> {
                    cury2 = data!!.getIntExtra("selectedCury", 0)
                    currencyChanged(CURY2_REQCODE)
                }
            }
        }
    }

    private fun currencyChanged(i: Int) {
        when (i) {
            CURY1_REQCODE -> {
                tvTotalAmount.text = getCurySymbol(curyOrderList[cury1].name) + "" + getPREFCurrencyBalance(curyOrderList[cury1].name)
                rate = 6.8765
                tvRate.text = rate.toString()
                curyEnName1.text = curyOrderList[cury1].name
                curyName1.text = getCuryString(curyOrderList[cury1].name)
                imgCury1.setImageResource(getCoinIconId(curyOrderList[cury1].name))
            }
            CURY2_REQCODE -> {
                rate = 0.4533
                tvRate.text = rate.toString()
                curyEnName2.text = curyOrderList[cury2].name
                curyName2.text = getCuryString(curyOrderList[cury2].name)
                imgCury2.setImageResource(getCoinIconId(curyOrderList[cury2].name))
            }
        }
        updateAmount()
    }

    private fun updateAmount() {
        try {
            var resultAmount = input.text.toString().toDouble() * rate
            tvResultAmount.text = NumberFormat.getNumberInstance().format(resultAmount)

        } catch (e: Exception) {
            tvResultAmount.text = "0"

        }
    }

    fun sellOut(view: View) {
        input.setText(getPREFCurrencyBalance(curyOrderList[cury1].name).toString())
    }

    fun btnExchange(view: View){
        finish()
    }

}
