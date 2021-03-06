package com.xinwang.xinwallet.presenter.activities.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.presenter.activities.HomeActivity
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import kotlinx.android.synthetic.main.activity_user_name.*

class SetUsernameActivity : XinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)

    }

    fun nextClicked(view: View) {

        if (etUserName.text.isBlank()) {
            SpringAnimator.failShakeAnimation(this, etUserName)
            Toast.makeText(this, getString(R.string.InvalidInput), Toast.LENGTH_LONG).show()
            return
        }

        val loader = LoaderDialogFragment()
        loader.show(supportFragmentManager, "LoaderDialogFragment")

        Profile().updateProfile(etUserName.text.trim().toString()) { result ->
            runOnUiThread {
                loader.dismiss()
                // var ok = true //todo : api didn't workn
                if (result!!) {
                    showSoftInput(false, etUserName) // don't show soft input again
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                    finish()
                } else {
                    SpringAnimator.failShakeAnimation(this, etUserName)
                }
            }
        }
    }
}
