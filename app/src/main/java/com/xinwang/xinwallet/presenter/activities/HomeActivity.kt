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
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.apiservice.XinWalletService
import com.xinwang.xinwallet.jsonrpc.FuncApp
import com.xinwang.xinwallet.jsonrpc.JSONRPC
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
    val TAG = "HomeActivity"

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
        balance.text = JSONRPC().getUserToken()
        println("$TAG+token_${JSONRPC().getUserToken()}")
    }

    fun getProfileData() {
        Profile().getProfile { status: Boolean, it ->
            if (it != null) {
                try {
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

                    var tmp = currencies.filter { currency ->
                        (currency.name == "BTC" ||currency.name == "ETH")
                    }
                    for (c in tmp){
                        println("????"+c.name)
                    }

                    // val list = currencies .sortedWith(compareBy({ it.order}))
                    doUI {
                        Glide.with(this).load(res.getString("avatar")).apply(RequestOptions().centerCrop().circleCrop()).into(avatar)
                    }
                } catch (e: Exception) {
                    Log.i(TAG, " getProfileData_$e")
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
        intent.setClass(this, BalanceListActivity::class.java)
        //  intent.setClass(this, EventTestActivity::class.java)
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
        loader.show(supportFragmentManager, "LoaderDialogFragment")
        when (requestCode) {
            123//相册
            -> {
                val dataUri = data.data
//                val cr = this.contentResolver
//                var bitmap = BitmapFactory.decodeStream(cr.openInputStream(dataUri))
                FuncApp().uploadAvatar(File(UriUtil().getPath(dataUri))) {
                    if (it) {
                        doUI {
                            getProfileData()
                            Toast.makeText(this, R.string.Home_changedAvatar, Toast.LENGTH_SHORT).show()
                            loader.dismiss()
                        }
                    }

                }

            }
        }
    }

}
