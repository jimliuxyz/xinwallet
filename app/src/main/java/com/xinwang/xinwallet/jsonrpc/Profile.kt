package com.xinwang.xinwallet.jsonrpc

import org.json.JSONObject
import java.util.ArrayList
import javax.security.auth.callback.Callback

class Profile :JSONRPC(){

    val domaim="profile"

    open fun updateProfile(name:String,callback: (result: Boolean?) -> Unit){
        val ss=  GenerateJsonRPCFormat.createJson("updateProfile", mapOf("keys" to arrayOf("name"),"values" to arrayOf(name))).toJsonString()

        super.send(domaim,ss){res ->

            var jsonObject: JSONObject? = JSONObject(res)
            println("updateprofile_"+res+"_"+jsonObject?.isNull("error"))
            if(jsonObject?.isNull("error")!!){
               callback(jsonObject.getBoolean("result"))
            }else{
                callback(false)
            }
        }

    }
}