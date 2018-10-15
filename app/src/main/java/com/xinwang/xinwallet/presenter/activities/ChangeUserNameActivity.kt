package com.xinwang.xinwallet.presenter.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.animation.SpringAnimator
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_change_user_name.*
import org.greenrobot.eventbus.EventBus

class ChangeUserNameActivity : XinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_name)
        titleSetting()
    }

    private fun titleSetting() {
        val backText = includeChangeName.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeChangeName.findViewById(R.id.title_name) as TextView
        val rightText = includeChangeName.findViewById(R.id.titleBarRightText) as TextView
        val backImg = includeChangeName.findViewById(R.id.imageBack) as ImageView
        titleBarText.text = getString(R.string.Profile_Name)
        backImg.visibility = View.GONE
        backText.visibility = View.GONE
        rightText.text = getString(R.string.Cancel)
        rightText.setOnClickListener {
            finish()
        }

    }

    fun btnOnClick(view: View) {
        if (etName.text.trim().isNotEmpty()) {
            val loader = LoaderDialogFragment()
            loader.show(supportFragmentManager, "LoaderDialogFragment")
            Profile().updateProfile(etName.text.trim().toString()) { result ->
                if (result!!) {


                    EventBus.getDefault().post(DataUpdateEvent(true, 1))
                    showSoftInput(false, etName) // don't show soft input again
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                    finish()
                } else {
                    doUI {
                        loader.dismiss()
                        SpringAnimator.failShakeAnimation(this, etName)
                    }
                }
            }

        } else {
            SpringAnimator.failShakeAnimation(this, etName)
            Toast.makeText(this, getString(R.string.InvalidInput), Toast.LENGTH_LONG).show()
            return
        }
    }
}


