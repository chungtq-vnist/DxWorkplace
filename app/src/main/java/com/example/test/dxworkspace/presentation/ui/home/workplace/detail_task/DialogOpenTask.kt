package com.example.test.dxworkspace.presentation.ui.home.workplace.detail_task

import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.task.RequestCloseTask
import com.example.test.dxworkspace.data.entity.task.TaskStatus
import com.example.test.dxworkspace.databinding.DialogOptionTaskBinding
import com.example.test.dxworkspace.databinding.DialogRequestCloseTaskBinding
import com.example.test.dxworkspace.databinding.DialogRequestReopenTaskBinding
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.initDialog

class DialogOpenTask(
    val context: Context

) {
    var binding: DialogRequestReopenTaskBinding? = null
    private var dialog: AlertDialog? = null
    var onConfirm: (() -> Unit)? = null

    init {
        val builder = AlertDialog.Builder(context)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_request_reopen_task,
            FrameLayout(context),
            false
        )
        builder.setView(binding!!.root)
        dialog = builder.create()
        dialog?.initDialog(false)
        binding?.apply {


            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
            btnConfirm.setOnClickListener {

                onConfirm?.invoke()
                dialog?.dismiss()
            }
        }

    }

    fun showDialog(){
        dialog?.show()
    }

    fun onDestroy(){
        dialog?.dismiss()
        dialog = null
    }


}