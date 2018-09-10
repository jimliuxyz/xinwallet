package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import com.google.gson.JsonObject
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.tools.util.doNetwork
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.setPref
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class FuncApp {

    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private val BASE_URL = "https://uwfuncapp-dev.azurewebsites.net/api/"
    private val TAG = "FuncApp"

    private val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()


    fun reqSmsVerify(phoneNo: String, callback: (status: Boolean, res: Any?) -> Unit) {
        val domain = "reqSmsVerify"
        val requestJSON = GenerateJsonRPCFormat.createJson("reqSmsVerify", mapOf("phoneno" to phoneNo)).toJsonString()

        doNetwork {
            val body = RequestBody.create(JSON, requestJSON)
            val request = Request.Builder().url(BASE_URL + domain)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + JSONRPC().getUserToken())
                    .build()
            val call = okHttpClient.newCall(request)
            getFuncAppResult(call) { status, res ->
                if (status) {
                    val resendCount = JSONObject(res.toString()).getInt("resendCount")
                    Log.i(TAG, "resendCount:$resendCount")
                    //紀錄驗證碼傳送時間
                    val time = System.currentTimeMillis()
                    XinWalletApp.instance.applicationContext.setPref(R.string.PREF_REQ_SMS_PASSCODE_TIME, time)
                    callback(true, resendCount)
                } else {
                    Log.i(TAG, "reqSmsVerify:$res")
                    JSONRPC().showToast("$res")
                    callback(false,"$res")
                }
            }
        }
    }


    fun getFuncAppResult(call: Call, cb: (status: Boolean, res: Any?) -> Unit) {
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.i(TAG, "getFuncAppResultFailure_$e")
                JSONRPC().showToast(e.toString())
                cb(false,e)
            }

            override fun onResponse(call: Call?, response: Response?) {
                try {
                    // var res: JsonObject? = null
                    val res = JSONObject(response?.body()?.string())
                    if (res.isNull("error")) {
                        cb(true, res.get("result"))
                    } else {
                        cb(false, res.get("error"))
                    }
                    Log.i(TAG, "getFuncAppResultResponse_$res")
                } catch (e: Exception) {
                    Log.i(TAG, "getFuncAppResultResponse2_$e")
                    JSONRPC().showToast(e.toString())
                    cb(false,e)
                }

            }
        })
    }


}