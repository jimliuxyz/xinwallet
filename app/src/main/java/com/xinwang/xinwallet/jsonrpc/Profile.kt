package com.xinwang.xinwallet.jsonrpc

import org.json.JSONObject
import java.util.ArrayList
import javax.security.auth.callback.Callback

 class Profile : JSONRPC() {

    val domaim = "profile"

    open fun updateProfile(name: String, callback: (result: Boolean?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("updateProfile", mapOf("keys" to arrayOf("name"), "values" to arrayOf(name))).toJsonString()

        super.send(domaim, ss) { res ->

            var jsonObject: JSONObject? = JSONObject(res)
            if (jsonObject?.isNull("error")!!) {
                callback(jsonObject.getBoolean("result"))
            } else {
                callback(false)
            }
        }

    }


    open fun getProfile(callback: (result: Any?) -> Unit) {

        val ss = GenerateJsonRPCFormat.createJson("getProfile", null).toJsonString()
        super.send(domaim, ss) {
            var jsonObject = JSONObject(it)
            if (jsonObject?.isNull("error")!!) {
                callback(jsonObject.getJSONObject("result"))
            } else {

                callback(null)
            }
        }


    }


}