package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.jsonrpc.Contacts

class AddFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        Contacts().finedUserByPhone(arrayOf("886919099069")){status, result ->
            
        }
          
            
            
            
        
    }
}
