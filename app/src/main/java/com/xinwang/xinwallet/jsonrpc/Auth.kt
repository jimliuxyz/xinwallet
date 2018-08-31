package com.xinwang.xinwallet.jsonrpc

import org.json.JSONObject


class Auth : JSONRPC() {

    open fun login(phoneno: String, passcode: String, callback: (result: String?) -> Unit) {
        super.send("auth", GenerateJsonRPCFormat.createJson("login", mapOf("phoneno" to phoneno, "passcode" to passcode, "pns" to "apns", "pnsToken" to "f607a1efa8ec3beb994d810a4b93623b81a257332aff8a9709990ba1611478c1")).toJsonString()) { res ->

            var token: String? = null
            var jsonObject: JSONObject? = JSONObject(res)
            println("onrespon" + jsonObject.toString())
            if (jsonObject?.isNull("error")!!) {
                var result: JSONObject? = jsonObject.getJSONObject("result")
                if (!result?.isNull("token")!!) {
                    token = result.getString("token")
                    println("token_"+token)
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