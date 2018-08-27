package com.xinwang.xinwallet.jsonrpc

import org.json.JSONObject
import java.util.ArrayList

class Trading : JSONRPC() {
    val domain: String = "trading"

    open fun getBalances(callback: (result: ArrayList<Currency>?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getBalances", null).toJsonString()
        super.send(domain, ss) { res ->
            var jsonObject: JSONObject? = JSONObject(res)
            println("result_${jsonObject.toString()}")
            if (jsonObject?.isNull("error")!!) {
                var jsonArray = jsonObject.getJSONArray("result")
                val objectCurrency: ArrayList<Currency>? = ArrayList<Currency>()
                for (i in 0..(jsonArray.length() - 1)) {
                    var obj: JSONObject = jsonArray.get(i) as JSONObject
                    var obj_name = obj.getString("name")
                    var obj_balance = obj.getDouble("balance")
                    // Todo   sort currency
                    (objectCurrency!! as ArrayList).add(i,Currency(obj_name, obj_balance))
                }
                callback(objectCurrency)

            } else {
               // callback(null)
            }
        }
    }
}

class Currency(val currencyName: String,
               var currencyBalance: Double) {

    var name: String = ""
    var balance: Double = 0.toDouble()

    init {
        name = this.currencyName
        balance = this.currencyBalance
    }


}