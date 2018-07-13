package com.xinwang.xinwallet;

import android.app.Application

/**
 * Created by jimliu on 2018/3/17.
 */
class XinWalletApp : Application() {

    companion object {
        private lateinit var instance_: XinWalletApp

        val instance: Application
            get() = instance_!!
    }

    override fun onCreate() {
        super.onCreate()
        instance_ = this
    }

}
