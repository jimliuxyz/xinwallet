package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.models.Currency
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Profile : JSONRPC() {

    val domaim = "profile"
    override var TAG = "Profile"

    open fun updateProfile(name: String, callback: (result: Boolean?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("updateProfile", mapOf("keys" to arrayOf("name"), "values" to arrayOf(name))).toJsonString()
        super.send(domaim, ss) { stauts: Boolean, res ->
            if (stauts) {
                try {
                    var jsonObject: JSONObject? = JSONObject(res)
                    if (jsonObject?.isNull("error")!!) {
                        callback(true)
                    } else {
                        callback(false)
                        Log.i(TAG, "updateProfile1_${jsonObject.getJSONObject("error")}")
                        showToast(jsonObject.getJSONObject("error").toString())
                    }
                } catch (e: Exception) {
                    callback(false)
                    Log.i(TAG, "updateProfile2_$e")
                    showToast(e.toString())
                }

            } else {
                //system error
                callback(false)
                Log.i(TAG, "updateProfile3_$res")
                showToast(res)
            }

        }
    }

    open fun getProfile(callback: (status: Boolean, result: Any?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getProfile", null).toJsonString()
        super.send(domaim, ss) { status, res ->
            if (status) {
                try {
                    var jsonObject = JSONObject(res)
                    if (jsonObject?.isNull("error")!!) {
                        callback(true, jsonObject.getJSONObject("result"))
                    } else {
                        Log.i(TAG, "getProfile1_${jsonObject.getJSONObject("error")}")
                        // showToast(jsonObject.getJSONObject("error").toString())
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "getProfile2_${e.toString()}")
                    showToast(e.toString())
                }
            } else {
                //system error
                Log.i(TAG, "getProfile3_$res")
                // showToast(res)
            }
            //  EventBus.getDefault().post(ApiDataEvent(0, ApiDataEvent.TYPE_PROFILE, jsonObject))
        }
    }

    fun updateCurrencySetting(parms: ArrayList<Currency>, callback: (result: Boolean?, res: String) -> Unit) {
        val requestJson = GenerateJsonRPCFormat.createJson("updateCurrencySetting", mapOf("list" to parms)).toJsonString()
        super.send(domaim, requestJson) { status, result ->
            callback(status, result)
            Log.i(TAG, "updateCurrencySetting_$result")
        }
    }

}