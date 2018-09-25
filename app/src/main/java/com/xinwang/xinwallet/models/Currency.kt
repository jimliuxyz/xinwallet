package com.xinwang.xinwallet.models

data class Currency(var name:String,var order:Int,var isDefault:Boolean) {
    constructor() : this("",-1,false)

    var balance: Double = 0.toDouble()

}