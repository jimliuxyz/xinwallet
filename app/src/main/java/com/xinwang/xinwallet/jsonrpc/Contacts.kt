package com.xinwang.xinwallet.jsonrpc

import com.google.gson.JsonObject

class Contacts :JSONRPC() {
    val domain="contacts"

    open fun getContacts(){
        val ss =GenerateJsonRPCFormat.createJson("getContacts",null).toJsonString()
        super.send(domain,ss){
            println("getcontacts_$it")

        }


    }


}