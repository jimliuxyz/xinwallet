package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import org.json.JSONObject


class Excurrency : JSONRPC() {
    val domain = "excurrency"
    override var TAG = "Excurrency"

    fun getExRate(callback: (status: Boolean, result: String?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getExRate", null).toJsonString()
        super.send(domain, ss) { status, result ->
            if (status) {
                var jsonObject: JSONObject? = JSONObject(result)
                if (jsonObject?.isNull("error")!!) {
                    val res = jsonObject.getJSONObject("result")
                    callback(true, res.toString())
                } else {
                    //error 回傳
                    Log.i(TAG, "getExRate1_${jsonObject.getJSONObject("error")}")
                    showToast(result)
                }
            } else {
                //system 錯誤
                Log.i(TAG, "getExRate2_$result")
                showToast(result)
            }
        }
    }


}