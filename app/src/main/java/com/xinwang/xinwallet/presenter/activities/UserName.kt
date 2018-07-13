package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_user_name.*

class UserName :  PinCodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)
    }

    fun nextClick(view : View){

        if(etUserName.text.length>0){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }else{

            Toast.makeText(this,"UserName is null",Toast.LENGTH_SHORT).show()
        }



    }
}
