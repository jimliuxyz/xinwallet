package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import com.xinwang.xinwallet.models.Currency
import org.json.JSONObject
import java.util.*

class Trading : JSONRPC() {
    private val domain: String = "trading"
    private val tag: String = "Trading"

    /*取得所餘額清單
    *
    * */
    open fun getBalancesList(callback: (result: ArrayList<Currency>?) -> Unit) {
        try {
            val ss = GenerateJsonRPCFormat.createJson("getBalances", null).toJsonString()
            super.send(domain, ss) { status: Boolean, res ->
                if (status) {
                    var jsonObject: JSONObject? = JSONObject(res)
                    if (jsonObject?.isNull("error")!!) {
                        var jsonArray = jsonObject.getJSONArray("result")
                        val currencyList: ArrayList<Currency>? = ArrayList<Currency>()
                        for (i in 0..(jsonArray.length() - 1)) {
                            var obj: JSONObject = jsonArray.get(i) as JSONObject
                            var currenctItem: Currency = Currency()
                            currenctItem.name = obj.getString("name")
                            currenctItem.balance = obj.getDouble("balance")
                            currencyList!!.add(currenctItem)
                        }
                        callback(currencyList)

                    } else {
                        callback(null)
                    }
                    //  EventBus.getDefault().post(ApiDataEvent(0, ApiDataEvent.TYPE_BALANCE, jsonObject))
                }
            }
        } catch (e: Exception) {
            Log.i(tag, "getBalancesList_$e")
        }

    }

    /*
    * 取得特定幣別餘額
    * currencyName:幣別
    *
    * */
    open fun getBalances(currencyName: String, callback: (result: Double?) -> Unit) {
        val ss = GenerateJsonRPCFormat.createJson("getBalances", null).toJsonString()
        super.send(domain, ss) { status: Boolean, res ->
            if (status) {
                try {
                    var jsonObject: JSONObject? = JSONObject(res)
                    if (jsonObject?.isNull("error")!!) {
                        var balance = jsonObject.getJSONObject("result")
                                .getJSONObject("list").getDouble(currencyName)
                        callback(balance)
                    } else {
                        showToast("${jsonObject.get("error")}")
                        Log.i(tag, "getBalances_${jsonObject.get("error")}")
                    }
                } catch (e: Exception) {
                    showToast("$e")
                    Log.i(tag, "getBalances2_$e")
                }
            } else {
                //系統錯誤
                showToast("$res")
                Log.i(tag, "getBalances3_$res")
            }
        }

    }

}

