package com.xinwang.xinwallet.models

import java.util.*

data class TransactionRecord(val id: String) {

    lateinit var datetime: Date
    var currency: String = ""
    lateinit var message: String
    var statusCode: Short = -1
    lateinit var statusMsg: String
    var txType: Short = -1
    var txParams=txParams()
    var txResult=txResult()

}

class txParams {
    var toCurrency: String = ""
    var sender: String = ""
    var receiver: String = ""
    var currency: String = ""
    var amount: Double = 0.0

}

class txResult {
    var outflow: Boolean = false
    var amount: Double = 0.0
    var balance: Double = 0.0


}