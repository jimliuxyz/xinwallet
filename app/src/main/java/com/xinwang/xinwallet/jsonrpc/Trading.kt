package com.xinwang.xinwallet.jsonrpc

import android.util.Log
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.models.Currency
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class Trading : JSONRPC() {
    private val domain: String = "trading"
    private val tag: String = "Trading"


    /*取得歷史交易紀錄
    *
    *
    * */
    fun getReceipts(date: Date, callback: (status: Boolean, result: String) -> Unit) {
        val requestJson = GenerateJsonRPCFormat.createJson("getReceipts", mapOf("fromDatetime" to "2018-09-09")).toJsonString()
        super.send(domain, requestJson) { status: Boolean, res: String ->
            if (status) {
                var jsonObject: JSONObject? = JSONObject(res)
                var resultString = jsonObject?.getJSONObject("result")!!

                callback(true, resultString.toString())
            } else {
                showToast("$res")
                Log.i(tag, "getReceipts_$res")
            }

        }
    }

    /*取得所餘額清單
    *
    * */
    fun getBalancesList(callback: (result: ArrayList<Currency>?) -> Unit) {
        try {
            val ss = GenerateJsonRPCFormat.createJson("getBalances", null).toJsonString()
            super.send(domain, ss) { status: Boolean, res ->
                if (status) {
                    var jsonObject: JSONObject? = JSONObject(res)
                    if (jsonObject?.isNull("error")!!) {
                        var jsonList = jsonObject.getJSONObject("result").getJSONObject("list")
                        val currencyList: ArrayList<Currency>? = ArrayList<Currency>()
                        val keys = jsonList.keys()
                        keys.forEach {
                            Log.i(TAG, "getBalancesList_${it}:${jsonList.getString(it)}")
                            val currencyItem = Currency()
                            currencyItem.name = it
                            currencyItem.balance = jsonList.getDouble(it)
                            currencyList!!.add(currencyItem)
                        }

                        callback(currencyList)

                    } else {
                        callback(null)
                    }
                    //  EventBus.getDefault().post(ApiDataEvent(0, ApiDataEvent.TYPE_BALANCE, jsonObject))
                }
            }
        } catch (e: Exception) {
            Log.i(tag, "getBalancesList2_$e")
        }

    }

    /*
    * 取得特定幣別餘額
    * currencyName:幣別
    *
    * */
    fun getBalances(currencyName: String, callback: (result: Double?) -> Unit) {
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

    /*
    * 存款
    * currencyName:幣別
    * amount:金額
    * */
    fun deposit(currencyName: String, amount: Double, callback: (status: Boolean, result: String) -> Unit) {

        val ss = GenerateJsonRPCFormat.createJson("deposit",
                mapOf("currency" to currencyName, "amount" to amount)).toJsonString()
        super.send(domain, ss) { status, result ->
            if (status) {
                var jsonObject: JSONObject? = JSONObject(result)
                if (jsonObject?.isNull("error")!!) {
                    var resultObj = jsonObject.getJSONObject("result")
                    EventBus.getDefault().post(DataUpdateEvent(true, DataUpdateEvent.BANLANCE))
                    callback(true, resultObj.toString())
                } else {
                    showToast("$result")
                    Log.i(tag, "deposit1_$result")
                }
            } else {
                showToast("$result")
                Log.i(tag, "deposit2_$result")
            }
        }

    }

    /*
   * 提款
   * currencyName:幣別
   * amount:金額
   * */
    fun withdraw(currencyName: String, amount: Double, callback: (status: Boolean, result: String) -> Unit) {

        val ss = GenerateJsonRPCFormat.createJson("withdraw",
                mapOf("currency" to currencyName, "amount" to amount)).toJsonString()
        super.send(domain, ss) { status, result ->
            if (status) {
                var jsonObject: JSONObject? = JSONObject(result)
                if (jsonObject?.isNull("error")!!) {
                    var resultObj = jsonObject.getJSONObject("result")
                    EventBus.getDefault().post(DataUpdateEvent(true,DataUpdateEvent.BANLANCE))
                    callback(true, resultObj.toString())
                } else {
                    showToast("$result")
                    Log.i(tag, "withdraw1_$result")
                }
            } else {
                showToast("$result")
                Log.i(tag, "withdraw2_$result")
            }
        }

    }


}

