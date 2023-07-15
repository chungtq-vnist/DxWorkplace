package com.example.test.dxworkspace.presentation.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.test.dxworkspace.R
import kotlinx.android.synthetic.main.dialog_cofirrm_notification.*

class DialogNotification (context: Context, val header:String,val content:String) : Dialog(context) {

    var callBack: CallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_cofirrm_notification)
        btn_logout.setOnClickListener(onClickListener)
        txt_title.text = header
        txt_content.text = content

    }

    private var onClickListener = View.OnClickListener {
        when (it.id) {

            R.id.btn_logout -> {
                dismiss()
            }
        }
    }

    interface CallBack {
        fun onLogoutCLickListener()
    }
}