package com.xinwang.xinwallet.presenter.activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.xinwang.xinwallet.R
import com.xinwang.xinwallet.presenter.activities.util.XinActivity
import kotlinx.android.synthetic.main.activity_exchange.*

class ExchangeActivity : XinActivity() {

    val CURY1_REQCODE=3489
    val CURY2_REQCODE=2394
    var cury1=0
    var cury2=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)
        titleSetting()
    }


    private fun titleSetting() {
        val backText = includeExchange.findViewById(R.id.txt_back) as TextView
        val titleBarText = includeExchange.findViewById(R.id.title_name) as TextView
        val rightText = includeExchange.findViewById(R.id.titleBarRightText) as TextView

        backText.text = getText(R.string.app_name)
        backText.setOnClickListener {
            finish()
        }
        titleBarText.text = getText(R.string.Exchange)
        rightText.visibility = View.GONE
    }

    fun selectCury(view: View) {
        val intent = Intent(this@ExchangeActivity, CurrencySelectActivity::class.java)
        when (view.id) {
            R.id.curyEnName1, R.id.curyName1, R.id.imgCury1, R.id.imgDown1 -> {
                intent.putExtra("selectedCury",cury1)
                startActivityForResult(intent,CURY1_REQCODE)

            }
            R.id.curyEnName2, R.id.curyName2, R.id.imgCury2, R.id.imgDown2 -> {
                intent.putExtra("selectedCury",cury2)
                startActivityForResult(intent,CURY2_REQCODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== Activity.RESULT_OK){
           when(requestCode){
               CURY1_REQCODE-> println("111111111111111_$CURY1_REQCODE")
               CURY2_REQCODE-> println("2222222222222222_$CURY2_REQCODE")
           }
        }
    }

}
