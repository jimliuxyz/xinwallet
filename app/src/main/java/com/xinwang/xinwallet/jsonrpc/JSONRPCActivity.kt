package com.xinwang.xinwallet.jsonrpc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import kotlinx.android.synthetic.main.activity_jsonrpc.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


/**
 * Created by Nate on 2018/8/16.
 */
class JSONRPCActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jsonrpc)
        contacts11()

    }

    private fun contacts11() {
     FuncApp().reqSmsVerify("886948488375"){status, res ->
         if(status){
             println("correct_${res.toString()}")
         }else{
             println("wang_${res.toString()}")
         }
     }
    }
//
//    private fun getProfile() {
//        Profile().getProfile {
//            var res = JSONObject(it.toString())
//            text00123.text = "getprofile_" + res.getString("currencies")
//
//        }
//    }
//
//    private fun loginTest() {
//        val loader = LoaderDialogFragment()
//        loader.show(supportFragmentManager, "LoaderDialogFragment")
//        Auth().login("745328901", "8888") { res ->
//            val ok = !res.isNullOrBlank() && !res.equals("null")
//            if (ok) {
//                text00123!!.text = res
//            }
//            loader.dismiss()
//
//        }
//    }
//
//    fun postOkHttp() {
//
////        Thread {
////
////
////            val JSON = MediaType.parse("application/json; charset=utf-8")
////            val body = RequestBody.create(JSON, json)
////
////            val client = OkHttpClient()
////            val resquest = Request.Builder().url("http://uwbackend-asia.azurewebsites.net/api/auth")
////                    .post(body)
////                    .build()
////            val call = client.newCall(resquest)
////            call.enqueue(object : Callback {
////                override fun onFailure(call: Call?, e: IOException?) {
////                    println("onfailure_" + e.toString())
////                }
////
////                override fun onResponse(call: Call?, response: Response?) {
////
////                    var jsonObject: JSONObject? = null
////                    var result: String? = null
////                    // System.out.println("output+>" + response.body().string());
////
////                    try {
////                        jsonObject = JSONObject(response?.body()!!.string())
////                        if (jsonObject.get("error").toString() == "null") {
////                            result = jsonObject.getJSONObject("result").get("token").toString()
////                        } else {
////                            result = jsonObject.getJSONObject("error").get("message").toString()
////                        }
////
////
////                    } catch (e: JSONException) {
////                        e.printStackTrace()
////                    }
////
////                    //   Object jsonOb = jsonObject.getJSONObject("result").get("token");
////
//////                    final String result =jsonOb.toString();
//////                    final  String id=jsonObject.getString("id");
////                    val ss = result
////
////
////                    println("onresponse_" + ss)
////
////                }
////            })
////
////
////        }.start()
//
//
//    }
//
//    fun longin(phoneNo: String, passcode: String) {
//
//        //1.包json字串
//
//        val json = "{\"jsonrpc\":\"2.0\"," +
//                "\"method\":\"login\"," +
//                "\"params\": {" +
//                "\"phoneno\":\"" + phoneNo + "\"," +
//                "\"passcode\":\"" + passcode + "\"},\"id\":\"99\"}"
//        println(json)
//
//
//        //2.post()
//
//        val JSON = MediaType.parse("application/json; charset=utf-8")
//        val body = RequestBody.create(JSON, json)
//
//        val resquest = Request.Builder().url("http://uwbackend-asia.azurewebsites.net/api/auth")
//                .post(body)
//                .build()
//
//
//        val client = OkHttpClient()
//
//        val call = client.newCall(resquest)
//
//        Thread {
//
//            call.enqueue(object : Callback {
//
//                override fun onFailure(call: Call?, e: IOException?) {
//                    println("onfailure_" + e.toString())
//                }
//
//                override fun onResponse(call: Call?, response: Response?) {
//
//                    var jsonObject: JSONObject? = null
//                    var result: String? = null
//                    // System.out.println("output+>" + response.body().string());
//
//                    try {
//                        jsonObject = JSONObject(response?.body()!!.string())
//                        if (jsonObject.get("error").toString() == "null") {
//                            result = jsonObject.getJSONObject("result").get("token").toString()
//                        } else {
//                            result = jsonObject.getJSONObject("error").get("message").toString()
//                        }
//
//
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//
//                    //  Object jsonOb = jsonObject.getJSONObject("result").get("token");
////                    final String result =jsonOb.toString();
////                    final  String id=jsonObject.getString("id");
//                    val ss = result
//
//                    println("onresponse_" + ss)
//
//                }
//            })
//        }.start()
//
//    }

}



