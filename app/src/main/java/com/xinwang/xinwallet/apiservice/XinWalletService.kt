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

    //Azure server base_Url
    private val BASE_URL = "https://uwfuncapp.azurewebsites.net/"

    private val ENCODE_KEY = "ASDFGHJKLASDFGHJ"
    private var USER_TOKEN = ""

    private var api: XinWalletWebApi

    companion object {
        val instance = XinWalletService()
    }

    private constructor() {

        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(Level.BASIC))
                .build()

        var retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        api = retrofit.create<XinWalletWebApi>(XinWalletWebApi::class.java)
    }

    /**
     *
     */
    private fun exceptionToErrMsg(e: Exception): String {
        return e.javaClass.simpleName
    }

    //發送簡訊驗證碼
    fun requestSMSVerify(phoneNo: String, callback: (status: String?, errmsg: String?) -> Unit) {
      //  val AZURE_CODE = "St0Av0A0PagU18UrTafewYxaZonjdrjnLQnTJVxVk6XhCh1lwUDC1A=="

        doNetwork {
            val call = api.requestSMSVerify(URLEncoder.encode(phoneNo, "utf-8"))
            var res: JsonObject? = null

            var errmsg = ""
            try {
                res = call.execute().body()

                var status = res!!.get("status").asString
                doUI {
                    callback(status, errmsg)
                }
                return@doNetwork
            } catch (e: Exception) {
                println("request return:" + "\n" + res?.toString())
                errmsg = exceptionToErrMsg(e).take(30)
                e.printStackTrace()
            }
            callback(null, errmsg)
        }

        val time = System.currentTimeMillis()
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_REQ_SMS_PASSCODE_TIME, time)
    }
    //取得SMS時間
    fun getRequestSMSVerifyTime(): Long {
        return XinWalletApp.instance.applicationContext.getPref(R.string.PREF_REQ_SMS_PASSCODE_TIME, 0L)
    }

    // 比對簡訊驗證法是否正確
    fun verifySMSPasscode(phoneNo: String, passcode: String, callback: (status: String?, errmsg: String?) -> Unit) {
        val AZURE_CODE = "lECM7Qzk08hMeMLmqIbosIfqQzHXAmZcialbxsT658huTitp8WUqxQ=="

        doNetwork {
            val call = api.verifySMSPasscode(AZURE_CODE
                    , URLEncoder.encode(phoneNo, "utf-8")
                    , URLEncoder.encode(passcode, "utf-8")
            )
            var res: JsonObject? = null

            var errmsg = ""
            try {
                //取得response json檔案
                res = call.execute().body()
                println(res)

                var status = res!!.get("status").asString
                var token = res!!.get("token").asString


                val ok = !status.isNullOrBlank() && status.equals("ok")
                // if(status==ok)將token存放本地端else 刪除token 資料
                if (ok)
                    setUserToken(token)
                else
                    delUserToken()

                doUI {
                    callback(status, errmsg)
                }
                return@doNetwork
            } catch (e: Exception) {
                println("request return:" + "\n" + res?.toString())
                errmsg = exceptionToErrMsg(e).take(30)
                e.printStackTrace()
            }
            callback(null, errmsg)
        }
    }

    fun setUserName(username: String, callback: (status: String?, errmsg: String?) -> Unit) {
        val AZURE_CODE = "lXyuIVo6awl56MAo2kIBF3NT1e9rMw4X5ybecNHrawksrKOHzC/XuQ=="
        doNetwork {
            val call = api.setUserName(AZURE_CODE, USER_TOKEN
                    , URLEncoder.encode(username, "utf-8")
            )
            var res: JsonObject? = null

            var errmsg = ""
            try {
                res = call.execute().body()
                println(res)

                var status = res!!.get("status").asString
                doUI {
                    callback(status, errmsg)
                }
                return@doNetwork
            } catch (e: Exception) {
                println("request return:" + "\n" + res?.toString())
                errmsg = exceptionToErrMsg(e).take(30)
                e.printStackTrace()
            }
            callback(null, errmsg)
        }
    }

    fun isPinCodeSetted(): Boolean {
        val md5 = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_PINCODE, "")
        return !md5.isNullOrBlank()
    }

    fun setPinCode(pincode: String) {
        setPinCodeDigits(pincode.length)
        val md5 = CryptoHelper.md5((ENCODE_KEY + pincode).toByteArray()).contentToString()
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_PINCODE, md5)
    }

    fun verifyPinCode(pincode: String): Boolean {
        val md5_ = CryptoHelper.md5((ENCODE_KEY + pincode).toByteArray()).contentToString()
        val md5 = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_PINCODE, "")

        return md5_.equals(md5)
    }

    private fun delPinCode() {
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_PINCODE, "")
    }

    private fun setPinCodeDigits(digits: Int) {
        XinWalletApp.instance.applicationContext.setPref(R.string.PREF_PINCODE_DIGITS, digits)
    }

    fun getPinCodeDigits(): Int {
        return XinWalletApp.instance.applicationContext.getPref(R.string.PREF_PINCODE_DIGITS, 4)
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
        delPinCode()
    }

    fun hasUserToken(): Boolean {
        val token = getUserToken()
        return !token.isNullOrBlank()
    }

    fun isLoginReady(): Boolean {
        return hasUserToken() && isPinCodeSetted()
    }
}
