package com.xinwang.xinwallet.jsonrpc

import okhttp3.*
import java.io.IOException


open class JSONRPC {
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

    fun send(method: String, requestJSON: String, callback: (result: String) -> Unit) {

        println("do posthttp ")
        // do PostHttp
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, requestJSON)

        val request = Request.Builder().url("http://uwbackend-asia.azurewebsites.net/api/auth")
                .post(body)
                .build()
        val call = OkHttpClient().newCall(request)

//        myCall(call, { res ->
//            println(res)
//        })

        getResult(call, { res -> callback(res.toString())})

    }


}