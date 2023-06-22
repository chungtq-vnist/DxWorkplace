package com.example.test.dxworkspace.presentation.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.presentation.utils.common.makeStatusBarTransparent

class DialogProcess(
        context: Context,
) : AlertDialog(context, R.style.DialogProcessStyle) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        setContentView(R.layout.dialog_process)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}