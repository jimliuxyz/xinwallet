package com.xinwang.xinwallet.jsonrpc

import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.busevent.ApiDataEvent
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Profile : JSONRPC() {

    val domaim = "profile"

    open fun updateProfile(name: String, callback: (result: Boolean?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("updateProfile", mapOf("keys" to arrayOf("name"), "values" to arrayOf(name))).toJsonString()

        super.send(domaim, ss) { res ->

            println("update_$res")
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
        println("getProfile_$ss")
        println()
        super.send(domaim, ss) {
            var jsonObject = JSONObject(it)
            println(jsonObject)
            if (jsonObject?.isNull("error")!!) {
                callback(jsonObject.getJSONObject("result"))
            } else {
                callback(null)
            }
            EventBus.getDefault().post(ApiDataEvent(0, ApiDataEvent.TYPE_PROFILE, jsonObject))
        }


    }


    fun uploadAvatar(image:File, callback: (result: String) -> Unit) {

        // do PostHttp
        val data = MediaType.parse("multipart/form-data")
        val requestBody = RequestBody.create(data, image)

        val multipartBody=MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("abc","abc.jpg",requestBody)
                .build()
        val request = Request.Builder().url("https://uwfuncapp.azurewebsites.net/api/uploadAvatar")
                .post(multipartBody)
                .addHeader("Authorization","Bearer "+ XinWalletService.instance.getUserToken())
                .build()


        val call = OkHttpClient().newCall(request)
        getResultAvator(call) { res -> callback(res.toString())}
    }


    fun getResultAvator(call: Call, cb:(res:Any?)->Unit){
        call.enqueue(object:Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("exeptionAvator_$e")
                cb(null)
            }

            override fun onResponse(call: Call?, response: Response?) {
                val res=response?.body()?.string()
                println("onResponseAvator_${res}")
                cb(res)
            }
        })
    }

}