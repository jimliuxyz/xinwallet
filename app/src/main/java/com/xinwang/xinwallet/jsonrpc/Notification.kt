package com.xinwang.xinwallet.jsonrpc

class Notification:JSONRPC() {
    val domain="notification"


    fun regPnsToken (callback: (result: String?) -> Unit) {
        val ss=GenerateJsonRPCFormat.createJson("regPnsToken", mapOf("pns" to "gcm","pnsToken" to "")).toJsonString()
        super.send(domain,ss){
            if(it.toBoolean()){
                callback(it)
            }else{


            }

        }

    }


}