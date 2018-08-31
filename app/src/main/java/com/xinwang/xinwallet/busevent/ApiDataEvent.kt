package com.xinwang.xinwallet.busevent

class ApiDataEvent(var errorCode: Int, var type: Int, var data: Any) {
    companion object {
        val TYPE_PROFILE = 1
        val TYPE_BALANCE = 2
    }
}
