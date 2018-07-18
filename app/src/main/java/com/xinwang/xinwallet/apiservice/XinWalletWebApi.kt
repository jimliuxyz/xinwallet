package com.xinwang.xinwallet.apiservice

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jimliu on 2018/7/12.
 */
interface XinWalletWebApi {

    @GET("api/requestSMSVerify")
    fun requestSMSVerify(
            @Query("code") code: String,
            @Query("phoneNo", encoded = true) phoneNo: String)
            : Call<JsonObject>

    @GET("api/verifySMSPasscode")
    fun verifySMSPasscode(
            @Query("code") code: String,
            @Query("phoneNo", encoded = true) phoneNo: String,
            @Query("smsCode", encoded = true) smsCode: String)
            : Call<JsonObject>

    @GET("api/setUsername")
    fun setUserName(
            @Query("code") code: String,
            @Query("token") token: String,
            @Query("name", encoded = true) name: String)
            : Call<JsonObject>
}
