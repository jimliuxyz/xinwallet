package com.xinwang.xinwallet

import android.app.Application
import android.content.Context

/**
 * Created by jimliu on 2018/3/17.
 */
class XinWalletApp : Application() {

    companion object {
        private lateinit var instance_: XinWalletApp

        lateinit var context: Context

        val instance: Application
            get() = instance_!!
    }

    override fun onCreate() {
        super.onCreate()
        instance_ = this
        context = applicationContext
    }


}
