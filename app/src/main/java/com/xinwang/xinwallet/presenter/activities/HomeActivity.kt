package com.xinwang.xinwallet.presenter.activities

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.tools.photo.UriUtil
import com.xinwang.xinwallet.jsonrpc.Profile
import com.xinwang.xinwallet.jsonrpc.Trading
import com.xinwang.xinwallet.models.Currency
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import com.xinwang.xinwallet.presenter.fragments.LoaderDialogFragment
import com.xinwang.xinwallet.tools.util.doUI
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.NumberFormat
import java.util.*


class HomeActivity : XinActivity() {

    val loader = LoaderDialogFragment()

    var currencies = ArrayList<Currency>()
    //千位數符號
    val numberFormat = NumberFormat.getNumberInstance()
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.Home_tag_friends)
                friendsActivity()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.Home_tag_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.Home_tag_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        getProfileData()

    }

    fun getProfileData() {
        Profile().getProfile {
            if (it != null) {
                var res = JSONObject(it.toString())
                val jsonArray = res.getJSONArray("currencies")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.get(i) as JSONObject
                    val currency = Currency(jsonObject.getString("name"), jsonObject.getInt("order"), jsonObject.getBoolean("isDefault"))
                    if (jsonObject.getBoolean("isDefault")) {
                        Trading().getBalances(jsonObject.getString("name")) {
                            doUI {
                                default_balance.text = numberFormat.format(it)
                            }
                        }
                        doUI {
                            default_currency.text = jsonObject.getString("name")
                        }
                    }
                    currencies.add(currency)
                }
                //     val list = currencies .sortedWith(compareBy({ it.order}))
                doUI {
                    Glide.with(this).load(res.getString("avatar")).apply(RequestOptions().centerCrop().circleCrop()).into(avatar)
                }
            }
        }
    }

    fun btnResetUserData(view: View) {
        XinWalletService.instance.delUserToken()
        exitApp()
    }

    fun balanceBtnClick(view: View) {

        var intent = Intent()
//        intent.setClass(this, BalanceListActivity::class.java)
        intent.setClass(this, EventTestActivity::class.java)
        //intent.putStringArrayListExtra("list",)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

    }

    fun friendsActivity() {

        var intent = Intent()
        intent.setClass(this, ContactsActivity::class.java)
        startActivity(intent)
    }

    fun changeAvator(view: View) {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            var intent = Intent()
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
            // 设置文件类型
            intent.setType("image/*")
            startActivityForResult(intent, 123)
        } else {
            EasyPermissions.requestPermissions(this@HomeActivity, "您需要打开读取相册权限", 34316, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            123//相册
            -> {

                val dataUri = data.data
//                val cr = this.contentResolver
//                var bitmap = BitmapFactory.decodeStream(cr.openInputStream(dataUri))
                Profile().uploadAvatar(File(UriUtil().getPath(dataUri))) {

                    doUI {
                        getProfileData()
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }


    fun getPath(uri: Uri): String? {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(this, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(this, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(this, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the returnData column for this Uri. This is useful for MediaStore Uris, and other file-based ContentProviders.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                val column_index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(column_index)
            }
        } catch (e: Exception) {
            //Log.e("UriUtil", "Unable to get path")
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }


    /**
     * Whether the Uri authority is ExternalStorageProvider.
     * 检测Uri是否是外部储存提供商
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


}
