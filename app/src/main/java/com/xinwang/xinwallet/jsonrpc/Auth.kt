package com.xinwang.xinwallet.jsonrpc

import org.json.JSONObject


class Auth : JSONRPC() {

    open fun login(phoneno: String, passcode: String, callback: (result: String?) -> Unit) {
        super.send("auth", GenerateJsonRPCFormat.createJson("login", mapOf("phoneno" to phoneno, "passcode" to passcode)).toJsonString()) { res ->

            var token: String? = null
            var jsonObject: JSONObject? = JSONObject(res)
            println("onrespon" + jsonObject.toString())
            if (jsonObject?.isNull("error")!!) {
                var result: JSONObject? = jsonObject.getJSONObject("result")
                if (!result?.isNull("token")!!) {
                    token = result.getString("token")
                    setUserToken(token)
                } else {
                    delUserToken()
                    token = null
                }
            }
            callback(token)
        }

    }

}