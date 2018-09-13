package com.xinwang.xinwallet.jsonrpc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.DBHelper
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
        createSQLite()

    }

    private fun createSQLite() {
        val helper = DBHelper(this, "test091315.db", null, 1)
        helper.insertUser()
        helper.readUsers()

    }


    private fun contacts11() {
        FuncApp().reqSmsVerify("886948488375") { status, res ->
            if (status) {
                println("correct_${res.toString()}")
            } else {
                println("wang_${res.toString()}")
            }
        }
    }


}



