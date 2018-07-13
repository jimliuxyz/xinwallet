package com.jimliuxyz.tsnote.services.translation

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jimliu on 2018/7/12.
 */
interface XinWalletWebApi {

    @GET("api/HttpTriggerCSharp3")
    fun requestSMSVerify(
            @Query("code") code: String,
            @Query("phoneNo", encoded = true) phoneNo: String)
            : Call<JsonObject>

    @GET("api/HttpTriggerDBtest")
    fun verifySMSPasscode(
            @Query("code") code: String,
            @Query("phoneNo", encoded = true) phoneNo: String,
            @Query("smsCode", encoded = true) smsCode: String)
            : Call<JsonObject>

}
