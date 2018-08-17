package com.xinwang.xinwallet.jsonrpc


class Auth : JSONRPC() {

    open fun login(phoneno: String, passcode: String, callback: (result: String) -> Unit) {
        super.send("auth",GenerateJsonRPCFormat.createJson("login", mapOf("phoneno" to phoneno,"passcode" to passcode)).toJsonString(),callback)
        println(GenerateJsonRPCFormat.createJson("login", mapOf("phoneno" to phoneno,"passcode" to passcode)).toJsonString())

    }

}