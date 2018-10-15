package com.xinwang.xinwallet.presenter.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.XinWalletApp
import com.xinwang.xinwallet.busevent.DataUpdateEvent
import com.xinwang.xinwallet.jsonrpc.FuncApp
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.photo.UriUtil
import com.xinwang.xinwallet.tools.util.doUI
import com.xinwang.xinwallet.tools.util.getPref
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class ProfileActivity : XinActivity() {

    val loader = LoaderDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getProfileDate()
        //EventBus subscriber
        EventBus.getDefault().register(this@ProfileActivity)

    }

    private fun getProfileDate() {
        val profileJson = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_MYPROFILE, "")

        var res = JSONObject(profileJson)
        tvProfileName.text = res.getString("name")
        if (res.getString("avatar").trim().isNotEmpty()) {
            Glide.with(this).load(res.getString("avatar"))
                    .apply(RequestOptions().centerCrop().circleCrop()).into(imgAvatar)
        }
        tvPhoneNo.text = res.getString("phoneno")
        tvID.text = res.getString("userId")
        titleSetting(res.getString("name"))

    }

    private fun titleSetting(name: String) {
        val backText = includeProfile.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeProfile.findViewById(R.id.title_name) as TextView
        val rightText = includeProfile.findViewById(R.id.titleBarRightText) as TextView
        titleBarText.text = name
        rightText.visibility = View.GONE
        backText.text = getText(R.string.Setting)
        backText.setOnClickListener {
            finish()
        }

    }

    fun changeNameOnClick(view: View) {
        val intent = Intent(this@ProfileActivity, ChangeUserNameActivity::class.java)
        startActivity(intent)
    }

    fun changeAvatarOnClick(view: View) {

        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            var intent = Intent()
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
            // 设置文件类型
            intent.setType("image/*")
            startActivityForResult(intent, 123)
        } else {
            EasyPermissions.requestPermissions(this@ProfileActivity, "您需要打开读取相册权限", 34316, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        when (requestCode) {
            123//相册
            -> {
                val dataUri = data.data
//                val cr = this.contentResolver
//                var bitmap = BitmapFactory.decodeStream(cr.openInputStream(dataUri))
                FuncApp().uploadAvatar(File(UriUtil().getPath(dataUri))) {
                    if (it) {
                        EventBus.getDefault().post(DataUpdateEvent(true, 1))
                        doUI {
                            // getProfileData()
                            Toast.makeText(this, R.string.Home_changedAvatar, Toast.LENGTH_SHORT).show()
                            loader.dismiss()
                        }
                    }

                }

            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: DataUpdateEvent) {
        when (event.type) {
            1 -> test()

        }
    }


    fun test() {
        updateProfileFromServer {
            if (it!!) {
                doUI {
                    getProfileDate()
                }
                val profileJson = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_MYPROFILE, "")
                println("testetstetstetets_$profileJson")
            } else {
                doUI {
                    Toast.makeText(this, "data_failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@ProfileActivity)
    }
}
