package com.xinwang.xinwallet.presenter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import java.util.*

class IntroActivity : XinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    var timer: Timer? = null

    override fun onResume() {
        super.onResume()

        timer = Timer()
        timer?.schedule(object : TimerTask(){
            override fun run() {
                val intent = Intent(this@IntroActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)

    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }
}
