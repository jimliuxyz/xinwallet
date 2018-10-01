package com.xinwang.xinwallet.presenter.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_tx_sort.*


class TxSortActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tx_sort)
        RadioGroupSetting()
    }

    private fun RadioGroupSetting() {
        RGTxType.setOnCheckedChangeListener { radioGroup, selectedID ->
           // val viewSelected= radioGroup.findViewById(selectedID) as RadioButton
           // radioButtonGroup.getChildAt(idx);
            for (i in 1..radioGroup.childCount){
               val view =radioGroup.getChildAt(i-1) as RadioButton
                if (view.isChecked){
                    view .setTextColor(getColor(R.color.wallet_orange))
                }else{
                    view.setTextColor(getColor(R.color.black))
                }

            }
           // radioGroup.getChildAt()
         //   view.setTextColor(getColor(R.color.wallet_orange))
            //Toast.makeText(this,i,Toast.LENGTH_SHORT).show()
            println("RGTx1>${RGTxType.checkedRadioButtonId}")
            println("RGTx2>${RGTxType.childCount}")
        }
    }
}
