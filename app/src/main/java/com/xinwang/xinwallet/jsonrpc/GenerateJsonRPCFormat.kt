package com.xinwang.xinwallet.jsonrpc

import com.google.gson.GsonBuilder

class GenerateJsonRPCFormat {
    val jsonrpc = "2.0"
    var id = ""

    var method: String? = null
    var params: Map<String, Any?>? = null

    companion object {

        fun createJson(method: String, params: Map<String, Any?>? = mapOf()): GenerateJsonRPCFormat {
            val jrpc = GenerateJsonRPCFormat()
            jrpc.method = method
            jrpc.params = params
            jrpc.id = Math.round(Math.random() * Short.MAX_VALUE).toString()
            return jrpc
        }


    }

    fun toJsonString(): String {
        var gson = GsonBuilder().create()
        return gson.toJson(this)
    }
}