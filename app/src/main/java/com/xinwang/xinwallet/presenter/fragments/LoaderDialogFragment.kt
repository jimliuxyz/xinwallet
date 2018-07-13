package com.xinwang.xinwallet.presenter.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.xinwang.xinwallet.R

class LoaderDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)

//        dialog.window.setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED
//        )

        return inflater?.inflate(R.layout.dialog_loader, container, false)
    }
}