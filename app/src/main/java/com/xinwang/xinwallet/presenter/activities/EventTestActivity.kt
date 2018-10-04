package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.busevent.ApiDataEvent
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.busevent.MyDataModel
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import kotlinx.android.synthetic.main.activity_event_test.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class EventTestActivity : AppCompatActivity() {

    private lateinit var eventBus: EventBus
    private val loader = LoaderDialogFragment()

    private val data = MyDataModel.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_test)

        eventBus = EventBus.getDefault()
        eventBus.register(this)
    }

    override fun onStart() {
        super.onStart()

        loader.show(supportFragmentManager, "LoaderDialogFragment")

//        Profile().getProfile {}
//        Trading().getBalancesList {}
    }

    private var map = mutableMapOf<Int, Int>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: DataUpdateEvent) {
        println("got event DataUpdateEvent type" + event.type)

        map.put(event.type, event.type)
        if (map.get(ApiDataEvent.TYPE_PROFILE) != null && map.get(ApiDataEvent.TYPE_BALANCE) != null) {
            tvText00.text = "balance:" + data.currency.balance + "\norder:" + data.currency.order
            Toast.makeText(this, "balance:" + data.currency.balance + "\norder:" + data.currency.order, Toast.LENGTH_LONG).show()
            loader.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventBus.unregister(this)
    }
}
