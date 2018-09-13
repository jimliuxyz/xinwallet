package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.tools.crypto.AESCipher
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import okhttp3.*
import java.io.IOException
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit


open class JSONRPC {

    val BASE_URL = "https://uwbackend-dev.azurewebsites.net/api/"
    private var USER_TOKEN = ""
    private val ENCODE_KEY = "ASDFGHJKLASDFGHJ"
    val client = OkHttpClient().newBuilder()
            .connectTimeout(35, TimeUnit.SECONDS)
            .build()

    open var TAG = "JSONRPC"

    fun getResult(call: Call, cb: (status: Boolean, res: Any?) -> Unit) {
        call.enqueue(object : Callback {
            //system錯誤 status->false
            override fun onFailure(call: Call?, e: IOException?) {
                cb(false, e.toString())
                Log.i(TAG, "onFailure()_" + e.toString())
            }

            //成功連上並有拋回物件 stats->true
            override fun onResponse(call: Call?, response: Response?) {
                try {
                    val result = response?.body()?.string()
                    cb(true, result)
                    Log.i(TAG, "onResponse()_$result")
                } catch (e: Exception) {
                    //有例外產生 status->false
                    cb(false, e.toString())
                    Log.i(TAG, "Exception_onResponse()_$e")
                }
            }
        })
    }

    // do PostHttp
    fun send(domain: String, requestJSON: String, callback: (status: Boolean, result: String) -> Unit) {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, requestJSON)
        val request = Request.Builder().url(BASE_URL + domain)
                .post(body)
                //header 加入JWT (Authorization:Bearer JWT)
                .addHeader("Authorization", "Bearer " + XinWalletService.instance.getUserToken())
                .build()
        val call = client.newCall(request)
        getResult(call) { status, res -> callback(status, res.toString()) }
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

    fun getUserToken(): String? {
        if (!USER_TOKEN.isNullOrBlank())
            return USER_TOKEN

        val cipher = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_USERTOKEN, "")
        if (cipher.isNullOrBlank())
            return null

        USER_TOKEN = AESCipher.decrypt(ENCODE_KEY, cipher)
        return USER_TOKEN
    }

    fun showToast(message: String) {
        doUI {
            Toast.makeText(XinWalletApp.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun JsonerrorIsNull(jsonString: String): Boolean {
        val jsonObject = JSONObject(jsonString)
        if (jsonObject.isNull("error")) {
            try {
                return true
            } catch (e: Exception) {
                Log.i(TAG, "getResultObject2_$e")
                return false
            }
        } else {
            //system error
            Log.i(TAG, "getResultObject3_${jsonObject["error"]}")
            // showToast(res)
            return false
        }
    }
}
