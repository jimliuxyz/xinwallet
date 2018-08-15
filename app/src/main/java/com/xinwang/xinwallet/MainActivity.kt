package com.xinwang.xinwallet

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  postOkHttp()
        //getOkHttp()
        longin("9873657658", "3333")

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


    fun getOkHttp() {
        Thread {
            val client = OkHttpClient()
            //  val request = Request.Builder().url("https://twilio168.azurewebsites.net/api/HttpTriggerDBtest?code=hNVOk/a7GCnVlrTxBACnEQsapW0SHFeswnuCzfI84aJl8MkDzPk/rA==&phoneNo=88000502&smsCode=3333").build()
            val request = Request.Builder().url("http://uwbackend-asia.azurewebsites.net/api/auth").build()
            val response = client.newCall(request).execute()
            val responsest = response.body()?.string()
            println("output_" + responsest)

            //runOnUiThread{ println(responsest)}
        }.start()
    }

}



