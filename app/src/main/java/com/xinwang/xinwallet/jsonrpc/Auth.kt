package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import org.json.JSONObject

class Auth : JSONRPC() {
    override var TAG = "Auth"

/*
* ss->建立json字串
*
* //送
{
    "jsonrpc": "2.0",
    "method": "login",
    "params": {
        "phoneno": "1234567890",
        "passcode": "8888",
        "pns": "gcm",
        "pnsToken": "f607a18c1"
    },
    "id": 99
}
//收
{
    "jsonrpc": "2.0",
    "result": {
        "token": "eyyZy93cy8yMDA1ZW50aXR"
    },
    "error": null
    "id": 99,
}
*
* */

    open fun login(phoneno: String, passcode: String, callback: (status: Boolean, result: String?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("login",
                mapOf("phoneno" to phoneno, "passcode" to passcode, "pns" to "gcm", "pnsToken" to "f607a1efa8ec3beb994d810a4b93623b81a257332aff8a9709990ba1611478c1"))
                .toJsonString()
        super.send("auth", ss) { status, res ->
            if (status) {//解析出token
                try {
                    var token: String? =null
                    var jsonObject: JSONObject? = JSONObject(res)
                    var result: JSONObject?
                    Log.i(TAG, "Auth_${jsonObject.toString()}")
                    if (jsonObject?.isNull("error")!!) {
                        //將JWT取出,回傳
                        result = jsonObject.getJSONObject("result")
                        token = result.getString("token")
                        setUserToken(token)
                        callback(true, token)
                    } else {
                        //取得error物件，並回傳status->false
                        result = jsonObject.getJSONObject("error")
                        callback(false, result.toString())
                    }

                }catch (e:Exception){
                    callback(false, e.toString())
                }

            } else {//系統錯誤
                callback(false, res)
            }

        }

    }


}