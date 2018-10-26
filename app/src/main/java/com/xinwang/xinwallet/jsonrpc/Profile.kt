package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import com.google.gson.Gson
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.models.Contacts
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.tools.util.getPref
import com.xinwang.xinwallet.tools.util.setPref
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Profile : JSONRPC() {

    val domaim = "profile"
    override var TAG = "Profile"
    val gson = Gson()

    /*
    * updateProfile
    * 更改姓名
    * name:姓名
    * P.S 完成此fun 即重新再更新SharedPreferences(PREF_MYPROFILE),
    * 並post DataUpdateEvent(DataUpdateEvent.PROFILE)
    * */
    fun updateProfile(name: String, callback: (result: Boolean?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("updateProfile", mapOf("keys" to arrayOf("name"), "values" to arrayOf(name))).toJsonString()
        super.send(domaim, ss) { stauts: Boolean, res ->
            if (stauts) {
                try {
                    var jsonObject: JSONObject? = JSONObject(res)
                    if (jsonObject?.isNull("error")!!) {
                        //取得遠端Profile資料並更新
                        Profile().getProfile { status, result ->
                            if (status) {
                                //update sharedPreferences
                                XinWalletApp.instance.applicationContext.setPref(R.string.PREF_MYPROFILE, result.toString())
                                //Publish event
                                EventBus.getDefault().post(DataUpdateEvent(true, DataUpdateEvent.PROFILE))
                                callback(true)
                                Log.i(TAG, "updateProfile1_${R.string.PREF_MYPROFILE} has updated")
                            } else {
                                callback(false)
                                Log.i(TAG, "updateProfile2_$result")
                            }
                        }
                    } else {
                        callback(false)
                        Log.i(TAG, "updateProfile3_${jsonObject.getJSONObject("error")}")
                        showToast(jsonObject.getJSONObject("error").toString())
                    }
                } catch (e: Exception) {
                    callback(false)
                    Log.i(TAG, "updateProfile4_$e")
                    showToast(e.toString())
                }

            } else {
                //system error
                callback(false)
                Log.i(TAG, "updateProfile5_$res")
                showToast(res)
            }

        }
    }

    fun getProfile(callback: (status: Boolean, result: Any?) -> Unit) {
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

    fun getUsersProfile(userIds: Array<String>, callback: (status: Boolean, result: ArrayList<Contacts>) -> Unit) {
        val requestJson = GenerateJsonRPCFormat.createJson("getUsersProfile", mapOf("userIds" to userIds)).toJsonString()
        super.send(domaim, requestJson) { status, result ->
            if (status) {
                var jsonObject = JSONObject(result)
                if (jsonObject?.isNull("error")!!) {
                    //取得查詢戶資料，if(聯絡人比數>0)回傳true else 回傳false
                    val array = jsonObject.getJSONObject("result").getJSONArray("list").toString()
                    val contactList = gson.fromJson(array, Array<Contacts>::class.java).toCollection(ArrayList())
                    if (contactList.isNotEmpty()) {
                        callback(true, contactList)
                    } else {
                        callback(false, contactList)
                    }
                }
                Log.i(TAG, "getUsersProfile1_$result")
            } else {
                Log.i(TAG, "getUsersProfile2_$result")
            }
        }

    }

}
