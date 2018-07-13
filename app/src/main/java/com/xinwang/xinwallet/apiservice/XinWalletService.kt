package com.jimliuxyz.tsnote.services.translation

import com.google.gson.JsonObject
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.tools.util.doNetwork
import com.xinwang.xinwallet.tools.util.doUI
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

    private var api: XinWalletWebApi

    private constructor() {

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

    companion object {
        val instance = XinWalletService()
    }

    fun requestSMSVerify(phoneNo: String, callback: (status:String?) -> Unit) {
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


    fun verifySMSPasscode(phoneNo: String, passcode: String, callback: (status:String?) -> Unit) {
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


//    fun setLocalValue(strResId: Int, value:String){
//        XinWalletApp.instance.applicationContext.setPref(strResId, value)
//    }

}
