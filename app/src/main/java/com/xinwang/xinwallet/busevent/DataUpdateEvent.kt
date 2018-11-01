package com.xinwang.xinwallet.busevent

open class DataUpdateEvent(var update: Boolean, var type: Int) {

    companion object {
        val PROFILE = 1
        val CURY_ORDER = 2
        val FRIENDS_LIST = 3
        val BANLANCE = 4
    }

}
