package com.xinwang.xinwallet.jsonrpc

import org.json.JSONException
import org.json.JSONObject

class Login : JSONRPC() {

    companion object {
        val url="http://uwbackend-asia.azurewebsites.net/api/auth"
        val method="login"
    }


     //呼叫jsonRPC
    fun sendResponse(phone:String,passCode:String) {
        super.send(url, getSendJson(phone, passCode), parseResponse)
    }

    //產生要傳送json格式
    fun getSendJson(phoneNo: String, passcode: String): String {
        return "{\"jsonrpc\":\"2.0\"," +
                "\"method\":\"login\"," +
                "\"params\": {" +
                "\"phoneno\":\"" + phoneNo + "\"," +
                "\"passcode\":\"" + passcode + "\"},\"id\":\"99\"}"
    }

    //處理callback函數，得到token
    val parseResponse = { res: String ->

        println("goodNate>>>>>>" + res)
        try {
            var jsonObject: JSONObject? = null
            var result: String? = null
            jsonObject = JSONObject(res)
            if (jsonObject.get("error").toString() == "null") {
                result = jsonObject.getJSONObject("result").get("token").toString()
            } else {
                result = jsonObject.getJSONObject("error").get("message").toString()
            }

            println("result_" + result)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }



}