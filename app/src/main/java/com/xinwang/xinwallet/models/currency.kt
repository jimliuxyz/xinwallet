package com.xinwang.xinwallet.models

class currency(var currencyName:String,var currencyOrder:Int,var currencyIsDefault:Boolean) {
    var name: String = ""
    var balance: Double = 0.toDouble()
    var order: Int? =null
    var isDefault:Boolean?=null

    init {
        name = this.currencyName
        order=this.currencyOrder
        isDefault =this.currencyIsDefault
    }
}