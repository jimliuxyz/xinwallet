package com.xinwang.xinwallet.jsonrpc

import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.tools.crypto.AESCipher
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import okhttp3.*
import java.io.IOException


open class JSONRPC {

    val BASE_URL = "https://uwbackend-asia.azurewebsites.net/api/"
    private var USER_TOKEN = ""
    private val ENCODE_KEY = "ASDFGHJKLASDFGHJ"

//    companion object {
//        fun getClinet(): JSONRPC {
//            return JSONRPC()
//        }
//    }


    fun getResult(call: Call, cb: (res: Any?) -> Unit) {
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                cb(null)
            }

            override fun onResponse(call: Call?, response: Response?) {
                cb(response?.body()?.string())
            }
        })
    }

    // do PostHttp
    fun send(domain: String, requestJSON: String, callback: (result: String) -> Unit) {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, requestJSON)
        val request = Request.Builder().url(BASE_URL + domain)
                .post(body)
                .addHeader("Authorization", "Bearer " + XinWalletService.instance.getUserToken())
                .build()
        val call = OkHttpClient().newCall(request)
        getResult(call) { res -> callback(res.toString()) }
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
}
