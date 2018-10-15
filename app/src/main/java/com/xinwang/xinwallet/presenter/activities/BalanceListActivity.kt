package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageView
import com.xinwang.xinwallet.R
import android.widget.TextView
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.models.adapter.CurrencyBaseAdapter
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import kotlinx.android.synthetic.main.activity_balance_list.*
import java.text.NumberFormat
import kotlinx.android.synthetic.main.currency_item.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


//千位數符號

class BalanceListActivity : XinActivity() {

    private val numberFormat = NumberFormat.getNumberInstance()
    private var defaultName: TextView? = null
    private var defaultAmount: TextView? = null
    private var defaultImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_list)
        settingTitleBar()
        defaultCurrencySetting()
        getCurrencyList()
        //EventBus subscriber
        EventBus.getDefault().register(this@BalanceListActivity)
    }

    private fun defaultCurrencySetting() {
        defaultName = includeTitleBarCurrencySetting.findViewById(R.id.txtNameCurrItem)
        defaultAmount = includeTitleBarCurrencySetting.findViewById(R.id.txtAmountCurrItem)
        defaultImage = includeTitleBarCurrencySetting.findViewById(R.id.ImgCurrItem)
        val firstCurName = getPREFCurrencyOrderList().filter { it.isDefault }[0].name
        defaultName!!.text = firstCurName
        val defaultAmtText = getCurySymbol(firstCurName) + numberFormat.format(getPREFCurrencyBalance(firstCurName)).toString()
        defaultAmount!!.text = defaultAmtText
        defaultImage!!.setImageResource(getCoinIconId(firstCurName))
        includeTitleBarCurrencySetting.setOnClickListener {
            val intent = Intent(this, CurrencyHomePage::class.java)
            intent.putExtra("currName", firstCurName)
            intent.putExtra("currAmount", defaultAmount!!.text)
            startActivity(intent)
        }
    }

    private fun settingTitleBar() {
        val backText = includeTitleBarBalanceList.findViewById(R.id.txt_back) as TextView?
        val titleBarText = includeTitleBarBalanceList.findViewById(R.id.title_name) as TextView?
        val rightText = includeTitleBarBalanceList.findViewById(R.id.titleBarRightText) as TextView?
        backText?.text = getString(R.string.app_name)
        titleBarText?.text = getString(R.string.Balance)
        rightText?.text = getString(R.string.Setting)
        rightText?.setOnClickListener {
            settingOnclick()
        }
        backText!!.setOnClickListener {
            val ooc = DataUpdateEvent(true, 990)
            EventBus.getDefault().post(ooc)
            finish()
        }

    }

    private fun getCurrencyList() {
        //非預設幣別清單
        val orderList = getPREFCurrencyOrderList()
        orderList.sortBy { it.order }
        orderList.removeAt(0)
        val adapter = CurrencyBaseAdapter(this, orderList)
        currencyList.adapter = adapter
        currencyList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, CurrencyHomePage::class.java)
            intent.putExtra("currName", view.txtNameCurrItem.text)
            intent.putExtra("currAmount", view.txtAmountCurrItem.text)
            startActivity(intent)
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }

    private fun settingOnclick() {
        val intent = Intent(this, CurrencySettingActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: DataUpdateEvent) {
        when (event.type) {
            2 -> {
                defaultCurrencySetting()
                getCurrencyList()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@BalanceListActivity)
    }
}
