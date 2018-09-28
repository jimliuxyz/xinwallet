package com.xinwang.xinwallet.models

import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import java.util.*

data class TransactionRecord(val id: String) {

    lateinit var datetime: Date
    var currency: String = ""
    lateinit var message: String
    var statusCode: Short = -1
    lateinit var statusMsg: String
    var txType: Short = -1
    var txParams = txParams()
    var txResult = txResult()

    fun getTxTypeName(): String {
        when (this.txType) {
            1.toShort() ->
                return XinWalletApp.instance.applicationContext.getString(R.string.Deposit)+"-"
            2.toShort() ->
                return XinWalletApp.instance.applicationContext.getString(R.string.Withdraw)+"-"
            3.toShort() ->
                return XinWalletApp.instance.applicationContext.getString(R.string.Transfer)+"-"
            4.toShort() ->
                return XinWalletApp.instance.applicationContext.getString(R.string.Exchange)+"-"
        }
        return ""
    }

}

class txParams {
    var toCurrency: String = ""
    var sender: String = ""
    var receiver: String = ""
    var currency: String = ""
    var amount: Double = 0.0

}

class txResult {
    var outflow: Boolean? = null
    var amount: Double = 0.0
    var balance: Double = 0.0

}