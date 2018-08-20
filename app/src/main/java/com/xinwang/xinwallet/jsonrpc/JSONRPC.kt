package com.xinwang.xinwallet.jsonrpc

import com.google.gson.GsonBuilder
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.tools.crypto.AESCipher
import com.xinwang.xinwallet.tools.util.setPref
import okhttp3.*
import org.json.JSONObject
import java.io.IOException



open class JSONRPC {

    val BASE_URL="http://uwbackend-asia.azurewebsites.net/api/"
    private var USER_TOKEN = ""
    private val ENCODE_KEY = "ASDFGHJKLASDFGHJ"

    companion object {
        fun getClinet(): JSONRPC {
            return JSONRPC()
        }
    }


    fun getResult(call:Call, cb:(res:Any?)->Unit){
        call.enqueue(object:Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                cb(null)
            }

            override fun onResponse(call: Call?, response: Response?) {
                var result :JSONObject?=null
                var jsonObject:JSONObject?= JSONObject(response?.body()?.string())
                println("onrespon"+jsonObject.toString())
                if (jsonObject?.get("error").toString() == "null") {
                    result = jsonObject!!.getJSONObject("result")

                }
                cb(result)
            }
        })
    }

    fun send(domain: String, requestJSON: String, callback: (result: String) -> Unit) {

        // do PostHttp
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, requestJSON)

        val request = Request.Builder().url(BASE_URL+domain)
                .post(body)
                .build()
        val call = OkHttpClient().newCall(request)

        getResult(call, { res -> callback(res.toString())})

    }


    fun setUserToken(token: String) {
        USER_TOKEN = token
        val cipher = AESCipher.encrypt(ENCODE_KEY, token)
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_USERTOKEN, cipher)
    }



    fun delUserToken() {
        USER_TOKEN = ""
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_USERTOKEN, "")
        delPinCode()
    }

    private fun delPinCode() {
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_PINCODE, "")
    }
//    fun generateJson(method:String,arg1:Any,arg2:Any):String{
//
//        //GenerateJsonRPCFormat.createJson(method,)
//        return "{\"jsonrpc\":\"2.0\"," +
//                "\"method\":\""+method+"\"," +
//                "\"params\": {" +
//                "\"phoneno\":\"" + "123485429" + "\"," +
//                "\"passcode\":\"" + "8888" + "\"},\"id\":\"99\"}"
//    }

}

class GenerateJsonRPCFormat {

    val jsonrpc = "2.0"
    var id = ""

    var method: String? = null
    var params: Map<String, Any?>? = null

    companion object {

        fun createJson(method: String, params: Map<String, Any?>? = mapOf()): GenerateJsonRPCFormat {
            val jrpc = GenerateJsonRPCFormat()
            jrpc.method = method
            jrpc.params = params
            jrpc.id = Math.round(Math.random() * Short.MAX_VALUE).toString()
            return jrpc
        }


    }

    fun toJsonString(): String {
        var gson = GsonBuilder().create()
        return gson.toJson(this)
    }


}