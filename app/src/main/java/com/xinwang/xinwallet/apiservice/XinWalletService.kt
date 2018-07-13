package com.xinwang.xinwallet.apiservice

import com.google.gson.JsonObject
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.tools.crypto.AESCipher
import com.xinwang.xinwallet.tools.crypto.CryptoHelper
import com.xinwang.xinwallet.tools.util.doNetwork
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder
import java.util.concurrent.TimeUnit


/**
 * Created by jimliu on 2018/7/12.
 */
class XinWalletService {

    private val BASE_URL = "https://twilio168.azurewebsites.net/"
//    private val AZURE_CODE = ""

    private val ENCODE_KEY = "XIN34524-1343"
    private var USER_TOKEN = ""

    private var api: XinWalletWebApi

    companion object {
        val instance = XinWalletService()
    }

    private constructor() {
        println("isPinCodeSetted() : " + this.isPinCodeSetted())
        println("hasUserToken() : " + this.hasUserToken())

        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(Level.BASIC))
                .build()

        var retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        api = retrofit.create<XinWalletWebApi>(XinWalletWebApi::class.java)
    }


    fun requestSMSVerify(phoneNo: String, callback: (status: String?) -> Unit) {
        val AZURE_CODE = "vsBbawBOQg3Ww0o7Mocv2mXOAcVwywv1NvCBGzmEkcGE5x9RXTHHcQ=="

        doNetwork {
            val call = api.requestSMSVerify(AZURE_CODE, URLEncoder.encode(phoneNo, "utf-8"))
            var res: JsonObject? = null

            try {
                res = call.execute().body()

                var status = res!!.get("status").asString
                doUI {
                    callback(status)
                }
                return@doNetwork
            } catch (e: Exception) {
                println("request return:" + "\n" + res?.toString())
                e.printStackTrace()
            }
            callback(null)
        }
    }


    fun verifySMSPasscode(phoneNo: String, passcode: String, callback: (status: String?) -> Unit) {
        val AZURE_CODE = "hNVOk/a7GCnVlrTxBACnEQsapW0SHFeswnuCzfI84aJl8MkDzPk/rA=="

        doNetwork {
            val call = api.verifySMSPasscode(AZURE_CODE
                    , URLEncoder.encode(phoneNo, "utf-8")
                    , URLEncoder.encode(passcode, "utf-8")
            )
            var res: JsonObject? = null

            try {
                res = call.execute().body()
                println(res)

                var status = res!!.get("status").asString
                doUI {
                    callback(status)
                }
                return@doNetwork
            } catch (e: Exception) {
                println("request return:" + "\n" + res?.toString())
                e.printStackTrace()
            }
            callback(null)
        }
    }

    fun isPinCodeSetted(): Boolean {
        val md5 = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_PINCODE, "")
        return !md5.isNullOrBlank()
    }

    fun setPinCode(pincode: String) {
        val md5 = CryptoHelper.md5((ENCODE_KEY + pincode).toByteArray()).contentToString()
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_PINCODE, md5)
    }

    fun verifyPinCodo(pincode_: String): Boolean {
        val md5_ = CryptoHelper.md5((ENCODE_KEY + pincode_).toByteArray()).contentToString()
        val md5 = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_PINCODE, "")

        return md5_.equals(md5)
    }

    fun delPinCode() {
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_PINCODE, "")
    }


    fun setUserToken(token: String) {
        USER_TOKEN = token
        val cipher = AESCipher.encrypt(ENCODE_KEY, token)
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_USERTOKEN, cipher)
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

    fun delUserToken() {
        USER_TOKEN = ""
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_USERTOKEN, "")
    }

    fun hasUserToken(): Boolean {
        val token = getUserToken()
        return !token.isNullOrBlank()
    }

}
