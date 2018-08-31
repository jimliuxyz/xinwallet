package com.xinwang.xinwallet.busevent

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MyDataModel private constructor() {

    private lateinit var eventBus: EventBus
    var currency = MyCurrency()


    companion object {
        private val model = MyDataModel()
        fun get() = model
    }

    init {
        eventBus = EventBus.getDefault()
        eventBus.register(this)
    }

    private fun load() {
        //load values/model back
    }

    private fun save() {
        //save values/model
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ApiDataEvent) {
        println("got event ApiDataEvent type" + event.type)

        if (event.errorCode != 0) {
            //handle error
            return
        }
        //dispatch data to parser
        else if (event.type == ApiDataEvent.TYPE_PROFILE)
            updateProfile(event.data)
        else if (event.type == ApiDataEvent.TYPE_BALANCE)
            updateBalance()
    }

    private fun updateProfile(data: Any) {
        //parse data and update to local model
        currency.order = 0

        //send a "data update" event to presenter/views
        EventBus.getDefault().post(DataUpdateEvent(0, ApiDataEvent.TYPE_PROFILE))
    }

    private fun updateBalance() {
        //parse data and update to local model
        currency.balance = 100

        //send a "data update" event to presenter/views
        EventBus.getDefault().post(DataUpdateEvent(0, ApiDataEvent.TYPE_BALANCE))
    }
}

class MyCurrency {
    var order = -1
    var balance = -1
}
