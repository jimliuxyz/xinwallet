package com.xinwang.xinwallet.jsonrpc

import org.json.JSONObject


class Auth : JSONRPC() {

    open fun login(phoneno: String, passcode: String, callback: (result: String?) -> Unit) {
        super.send("auth",GenerateJsonRPCFormat.createJson("login", mapOf("phoneno" to phoneno,"passcode" to passcode)).toJsonString()) { res ->

            var token: String?
            if (res.equals("null")) {
                token = null
                delUserToken()
            } else {
                var jsonObject: JSONObject? = null
                jsonObject = JSONObject(res)
                token = jsonObject.get("token").toString()
                setUserToken(token)
            }
            callback(token)
        }


        println(GenerateJsonRPCFormat.createJson("login", mapOf("phoneno" to phoneno,"passcode" to passcode)).toJsonString())
    }

}