package com.xinwang.xinwallet.jsonrpc
import com.xinwang.xinwallet.models.Contacts
import org.json.JSONObject
import java.util.ArrayList

class Contacts : JSONRPC() {
    val domain = "contacts"

    open fun getContactsList(callback: (result: ArrayList<Contacts>?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getContacts", null).toJsonString()
        super.send(domain, ss) {

            var jsonObject: JSONObject? = JSONObject(it)
            if (jsonObject?.isNull("error")!!) {
                var  jsonObject1=jsonObject.getJSONObject("result")
                var jsonArray = jsonObject1.getJSONArray("contacts")
                var contactList: ArrayList<Contacts>? = ArrayList<Contacts>()
                for (i in 0..(jsonArray.length() - 1)) {
                    var obj: JSONObject = jsonArray.get(i) as JSONObject
                    var item:Contacts = Contacts()
                    item.userId = obj.getString("userId")
                    item.name = obj.getString("name")
                    item.avatar=obj.getString("avatar")
                    contactList!!.add(item)
                }
                callback(contactList)

            } else {
                callback(null)
            }

        }


    }


}