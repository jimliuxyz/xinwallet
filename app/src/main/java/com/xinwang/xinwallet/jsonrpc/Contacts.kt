package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import android.widget.Toast
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.models.Contacts
import org.json.JSONObject
import java.util.ArrayList

class Contacts : JSONRPC() {
    val domain = "contacts"
    override var TAG = "Contacts"

    open fun getContactsList(callback: (status: Boolean, result: ArrayList<Contacts>?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getContacts", null).toJsonString()
        super.send(domain, ss) { status, it ->
            if (status) {
                try {
                    var jsonObject: JSONObject? = JSONObject(it)
                    if (jsonObject?.isNull("error")!!) {
                        var jsonObject1 = jsonObject.getJSONObject("result")
                        var jsonArray = jsonObject1.getJSONArray("contacts")
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

}