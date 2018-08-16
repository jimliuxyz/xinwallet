package com.xinwang.xinwallet.jsonrpc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xinwang.xinwallet.R
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
        loginTest()

    }

    private fun loginTest() {
        Login().sendResponse("0943844765","2345")
    }


    fun postOkHttp() {

//        Thread {
//
//
//            val JSON = MediaType.parse("application/json; charset=utf-8")
//            val body = RequestBody.create(JSON, json)
//
//            val client = OkHttpClient()
//            val resquest = Request.Builder().url("http://uwbackend-asia.azurewebsites.net/api/auth")
//                    .post(body)
//                    .build()
//            val call = client.newCall(resquest)
//            call.enqueue(object : Callback {
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
//                    //   Object jsonOb = jsonObject.getJSONObject("result").get("token");
//
////                    final String result =jsonOb.toString();
////                    final  String id=jsonObject.getString("id");
//                    val ss = result
//
//
//                    println("onresponse_" + ss)
//
//                }
//            })
//
//
//        }.start()


    }


    fun longin(phoneNo: String, passcode: String) {


        //1.包json字串

        val json = "{\"jsonrpc\":\"2.0\"," +
                "\"method\":\"login\"," +
                "\"params\": {" +
                "\"phoneno\":\"" + phoneNo + "\"," +
                "\"passcode\":\"" + passcode + "\"},\"id\":\"99\"}"
        println(json)


        //2.post()

        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, json)

        val resquest = Request.Builder().url("http://uwbackend-asia.azurewebsites.net/api/auth")
                .post(body)
                .build()


        val client = OkHttpClient()

        val call = client.newCall(resquest)

        Thread {

            call.enqueue(object : Callback {

                override fun onFailure(call: Call?, e: IOException?) {
                    println("onfailure_" + e.toString())
                }

                override fun onResponse(call: Call?, response: Response?) {

                    var jsonObject: JSONObject? = null
                    var result: String? = null
                    // System.out.println("output+>" + response.body().string());

                    try {
                        jsonObject = JSONObject(response?.body()!!.string())
                        if (jsonObject.get("error").toString() == "null") {
                            result = jsonObject.getJSONObject("result").get("token").toString()
                        } else {
                            result = jsonObject.getJSONObject("error").get("message").toString()
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    //  Object jsonOb = jsonObject.getJSONObject("result").get("token");
//                    final String result =jsonOb.toString();
//                    final  String id=jsonObject.getString("id");
                    val ss = result

                    println("onresponse_" + ss)

                }
            })
        }.start()

    }


    fun postHttp(request: Request): Call? {

        return null
    }



}