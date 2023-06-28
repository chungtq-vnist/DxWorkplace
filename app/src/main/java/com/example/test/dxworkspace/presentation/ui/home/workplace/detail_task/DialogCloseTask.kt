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
import com.example.test.dxworkspace.data.entity.task.RequestToCloseTaskResponse
import com.example.test.dxworkspace.data.entity.task.TaskStatus
import com.example.test.dxworkspace.databinding.DialogRequestCloseTaskBinding
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.initDialog

class DialogCloseTask(
    val context: Context,
    val taskStatus : String = "",
    val isCancelRequest: Boolean = false,
    val taskRequestClose : RequestToCloseTaskResponse?

) {
    var binding: DialogRequestCloseTaskBinding? = null
    private var dialog: AlertDialog? = null
    var onConfirm: ((RequestCloseTask) -> Unit)? = null

    init {
        val builder = AlertDialog.Builder(context)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_request_close_task,
            FrameLayout(context),
            false
        )
        builder.setView(binding!!.root)
        dialog = builder.create()
        dialog?.initDialog(false)
        binding?.apply {

            val adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                listOf(
                    TaskStatus.wait_for_approval,
                    TaskStatus.finished,
                    TaskStatus.delayed,
                    TaskStatus.canceled
                )
            )
            tvStatus.adapter = adapter
            if (isCancelRequest) {
                btnConfirm.text = "Hủy yêu cầu"
                btnConfirm.setTextColor(context.resources.getColor(R.color.red))
                tvStatus.setSelection(when(taskStatus){
                    TaskStatus.wait_for_approval -> 0
                    TaskStatus.finished -> 1
                    TaskStatus.delayed -> 2
                    TaskStatus.canceled -> 3
                    else -> 0
                })
                tvStatus.isClickable = false
                tvStatus.isEnabled = false
                edtDes.setText(taskRequestClose?.description)
                edtDes.isEnabled = false

            }
            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
            btnConfirm.setOnClickListener {
                val v = RequestCloseTask(1)
                if (isCancelRequest) {
                    v.type = "cancel_request"
                } else {
                    v.type = "request"
                    when (tvStatus.selectedItemPosition) {
                        0 -> {
                            v.taskStatus = "wait_for_approval"
                        }
                        1 -> {
                            v.taskStatus = "finished"
                        }
                        2 -> {
                            v.taskStatus = "delayed"
                        }
                        3 -> {
                            v.taskStatus = "canceled"
                        }
                    }
                    v.description = edtDes.getTextz()
                }
                onConfirm?.invoke(v)
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