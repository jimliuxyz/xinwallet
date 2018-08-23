package com.xinwang.xinwallet.jsonrpc

import com.google.gson.GsonBuilder
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.tools.crypto.AESCipher
import com.xinwang.xinwallet.tools.util.setPref
import okhttp3.*
import org.json.JSONObject
import java.io.IOException



open class JSONRPC {

    val BASE_URL="https://uwbackend-asia.azurewebsites.net/api/"
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
                cb(response?.body()?.string())
            }
        })
    }

    fun send(domain: String, requestJSON: String, callback: (result: String) -> Unit) {

        // do PostHttp
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, requestJSON)

        val request = Request.Builder().url(BASE_URL+domain)
                .post(body)
              //.addHeader("Authorization","Bearer "+ XinWalletService.instance.getUserToken())
                .addHeader("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9tb2JpbGVwaG9uZSI6IjM4NzU2MzkiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiMzg3NTYzOSIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IlVzZXIiLCJ1c2VyaWQiOiI5ODBjM2Q5Yy0yNGY4LTQ3ZTEtYmFhZC1hYzM1Y2Y5MGZlZjQiLCJpc3MiOiJ4aW53YW5nIiwiYXVkIjoidXdhbGxldCJ9.A24Yne2xUN3TQ0-aKfY5XQaDA6mVIV7Nw5YEs-f92LI")
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


}

//產生jsonRPC類別
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