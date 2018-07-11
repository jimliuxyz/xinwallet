package com.xinwang.xinwallet

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Thread {
            val client = OkHttpClient()
            val request = Request.Builder().url("https://twilio168.azurewebsites.net/api/HttpTriggerDBtest?code=hNVOk/a7GCnVlrTxBACnEQsapW0SHFeswnuCzfI84aJl8MkDzPk/rA==&phoneNo=88000502&smsCode=3333").build()
            val response = client.newCall(request).execute()
            val responsest=response.body()?.string()
            println(responsest)

            //runOnUiThread{ println(responsest)}
        }.start()
     }


}

