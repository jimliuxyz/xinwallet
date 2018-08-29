package com.xinwang.xinwallet.jsonrpc

import com.xinwang.xinwallet.models.Currency
import org.json.JSONObject
import java.util.ArrayList
import javax.security.auth.callback.Callback

class Trading : JSONRPC() {
    val domain: String = "trading"

/*取得所餘額清單
*
* */
    open fun getBalancesList(callback: (result: ArrayList<Currency>?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getBalances", null).toJsonString()
        super.send(domain, ss) { res ->
            var jsonObject: JSONObject? = JSONObject(res)
            if (jsonObject?.isNull("error")!!) {
                var jsonArray = jsonObject.getJSONArray("result")
                val currencyList: ArrayList<Currency>? = ArrayList<Currency>()
                for (i in 0..(jsonArray.length() - 1)) {
                    var obj: JSONObject = jsonArray.get(i) as JSONObject
                    var currenctItem:Currency=Currency()
                    currenctItem.name=obj.getString("name")
                    currenctItem.balance=obj.getDouble("balance")
                    currencyList!!.add(currenctItem)
                }
                callback(currencyList)

            } else {
                 callback(null)
            }
        }
    }


    /*
    * 取得特定幣別餘額
    * currencyName:幣別
    *
    * */
    open fun getBalances(currencyName: String,callback: (result: Double?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getBalances", null).toJsonString()
        super.send(domain, ss) { res ->
            var jsonObject: JSONObject? = JSONObject(res)
            println("result_${jsonObject.toString()}")
            if (jsonObject?.isNull("error")!!) {
                var jsonArray = jsonObject.getJSONArray("result")
                for (i in 0..(jsonArray.length() - 1)) {
                    var obj: JSONObject = jsonArray.get(i) as JSONObject
                    if (obj.getString("name").equals(currencyName)) {
                        callback(obj.getDouble("balance"))
                    }
                }

            } else {
                // callback(null)
            }
        }

    }

}

