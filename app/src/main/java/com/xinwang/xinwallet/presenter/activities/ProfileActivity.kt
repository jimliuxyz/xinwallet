package com.xinwang.xinwallet.presenter.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
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
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.davemorrissey.labs.subscaleview.ImageSource

class ProfileActivity : XinActivity() {

    val REQUEST_CODE_ALBUM = 3498
    val REQUEST_CODE_CAMER = 8430

    val loader = LoaderDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getProfileDate()
        //EventBus subscriber
        EventBus.getDefault().register(this@ProfileActivity)
        tvPhoneNo.setOnClickListener {
            startCamera()
        }
        println("file path=>${cacheDir}")
    }

    private fun startCamera() {

//
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                var photoUrl: Uri = Uri.fromFile(File(Environment.getExternalStorageDirectory().path + "/camera_picture.jpg"))
//                var builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
//                StrictMode.setVmPolicy(builder.build())
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUrl)
//                startActivityForResult(takePictureIntent, REQUEST_CODE_CAMER)
//            }

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    var photoUrl: Uri = Uri.fromFile(File(Environment.getExternalStorageDirectory().path+"/camera_picture.jpg"))
                    var builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
                    StrictMode.setVmPolicy(builder.build())
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUrl)
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAMER)
                }
            }
//            //指定相机意图
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(intent, REQUEST_CODE_CAMER)
        }

    }

    private fun getProfileDate() {
        val profileJson = XinWalletApp.instance.applicationContext.getPref(R.string.PREF_MYPROFILE, "")

        if (!profileJson.equals("")) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        // loader.show(supportFragmentManager, "LoaderDialogFragment")
        when (requestCode) {
            123 -> {
                val dataUri = data!!.data
//                val cr = this.contentResolver
//                var bitmap = BitmapFactory.decodeStream(cr.openInputStream(dataUri))
                FuncApp().uploadAvatar(File(UriUtil().getPath(dataUri))) {
                    if (it) {
                        EventBus.getDefault().post(DataUpdateEvent(true, 1))
                        doUI {
                            // getProfileData()
                            Toast.makeText(this, R.string.Home_changedAvatar, Toast.LENGTH_SHORT).show()
                            // loader.dismiss()
                        }
                    }

                }
            }
            REQUEST_CODE_CAMER -> {
                val uri=Environment.getExternalStorageDirectory().path + "/camera_picture.jpg"
                imageView007.setImage(ImageSource.uri(uri))
//                // loader.dismiss()
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: DataUpdateEvent) {
        when (event.type) {
            DataUpdateEvent.PROFILE -> getProfileDate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@ProfileActivity)
    }
}
