package com.xinwang.xinwallet.models

open class Currency(var currencyName:String,var currencyOrder:Int,var currencyIsDefault:Boolean) {
    constructor() : this("",-1,false)

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