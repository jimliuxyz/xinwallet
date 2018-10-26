package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.models.Contacts
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.util.*

class Contacts : JSONRPC() {
    val domain = "contacts"
    override var TAG = "Contacts"

    fun getContactsList(callback: (status: Boolean, result: ArrayList<Contacts>?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getContacts", null).toJsonString()
        super.send(domain, ss) { status, it ->
            if (status) {
                try {
                    var jsonObject: JSONObject? = JSONObject(it)
                    if (jsonObject?.isNull("error")!!) {
                        var jsonObject1 = jsonObject.getJSONObject("result")
                        var jsonArray = jsonObject1.getJSONArray("list")
                        var contactList: ArrayList<Contacts>? = ArrayList<Contacts>()
                        for (i in 0..(jsonArray.length() - 1)) {
                            var obj: JSONObject = jsonArray.get(i) as JSONObject
                            var item: Contacts = Contacts()
                            item.userId = obj.getString("userId")
                            item.name = obj.getString("name")
                            item.avatar = obj.getString("avatar")
                            contactList!!.add(item)
                        }
                        callback(true, contactList)
                    } else {
                        //error 回傳
                        Log.i(TAG, "getContactsList1_${jsonObject.getJSONObject("error")}")
                        showToast(it)
                    }
                } catch (e: Exception) {
                    //json 錯誤
                    Log.i(TAG, "getContactsList2_$e")
                    showToast(it)
                }
            } else {
                //system 錯誤
                Log.i(TAG, "getContactsList3_$it")
                showToast(it)
            }
        }
    }

    fun finedUserByPhone(phoneArray: Array<String>, callback: (status: Boolean, result: ArrayList<Contacts>?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("findUsersByPhone", mapOf("list" to phoneArray)).toJsonString()
        super.send(domain, ss) { status, res ->
            if (status) {
                callback(true, parseJsonToContacts(res))
                Log.i(TAG, "finedUserByPhone1_$res")
            } else {
                //system 錯誤
                Log.i(TAG, "finedUserByPhone2_$res")
                showToast(res)
            }
        }
    }

    private fun parseJsonToContacts(it: String): ArrayList<Contacts>? {
        try {
            var jsonObject: JSONObject? = JSONObject(it)
            if (jsonObject?.isNull("error")!!) {
                var jsonObject1 = jsonObject.getJSONObject("result")
                var jsonArray = jsonObject1.getJSONArray("list")
                var contactList: ArrayList<Contacts>? = ArrayList<Contacts>()
                for (i in 0..(jsonArray.length() - 1)) {
                    var obj: JSONObject = jsonArray.get(i) as JSONObject
                    var item: Contacts = Contacts()
                    item.userId = obj.getString("userId")
                    item.name = obj.getString("name")
                    item.avatar = obj.getString("avatar")
                    contactList!!.add(item)
                }
                return contactList
            } else {
                //error 回傳
                Log.i(TAG, "getContactsList1_${jsonObject.getJSONObject("error")}")
                showToast(it)
            }
        } catch (e: Exception) {
            //json 錯誤
            Log.i(TAG, "getContactsList2_$e")
            showToast(it)
        }
        return null
    }

    //新增聯絡人
    fun addFriends(list: ArrayList<String>, callback: (result: Boolean?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("addFriends", mapOf("list" to list)).toJsonString()
        super.send(domain, ss) { status, res ->
            if (status && JsonerrorIsNull(res)) {
                //Publish event
                EventBus.getDefault().post(DataUpdateEvent(true, DataUpdateEvent.FRIENDS_LIST))
                callback(true)
            } else {
                showToast(res)
                Log.i(TAG, "addFriends_$res")
            }

        }
    }

    fun getAllContactsList(callback: (status: Boolean, result: ArrayList<Contacts>?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getAllUsers", null).toJsonString()
        super.send(domain, ss) { status, it ->
            if (status) {
                try {
                    var jsonObject: JSONObject? = JSONObject(it)
                    if (jsonObject?.isNull("error")!!) {
                        var jsonObject1 = jsonObject.getJSONObject("result")
                        var jsonArray = jsonObject1.getJSONArray("list")
                        var contactList: ArrayList<Contacts>? = ArrayList<Contacts>()
                        for (i in 0..(jsonArray.length() - 1)) {
                            var obj: JSONObject = jsonArray.get(i) as JSONObject
                            var item: Contacts = Contacts()
                            item.userId = obj.getString("userId")
                            item.name = obj.getString("name")
                            item.avatar = obj.getString("avatar")
                            contactList!!.add(item)
                        }
                        callback(true, contactList)
                    } else {
                        //error 回傳
                        Log.i(TAG, "getContactsList1_${jsonObject.getJSONObject("error")}")
                        showToast(it)
                    }
                } catch (e: Exception) {
                    //json 錯誤
                    Log.i(TAG, "getContactsList2_$e")
                    showToast(it)
                }
            } else {
                //system 錯誤
                Log.i(TAG, "getContactsList3_$it")
                showToast(it)
            }
        }
    }

    fun delFriends(list: ArrayList<String>, callback: (result: Boolean?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("delFriends", mapOf("list" to list)).toJsonString()
        super.send(domain, ss) { status, result ->
            if (status && JsonerrorIsNull(result))  {
                //Publish event
                EventBus.getDefault().post(DataUpdateEvent(true, DataUpdateEvent.FRIENDS_LIST))
                callback(true)
            } else {
                showToast(result)
                Log.i(TAG, "delFriends_$result")
            }
        }
    }


}