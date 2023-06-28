package com.example.test.dxworkspace.presentation.ui.home.workplace.detail_task

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.DialogOptionTaskBinding
import com.example.test.dxworkspace.presentation.utils.common.UIHelper

class DialogTaskOption(
    val context: Context,
    private val onItemClick: OnBillOptionListener,
) {

    private var binding: DialogOptionTaskBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.dialog_option_task,
        FrameLayout(context),
        false
    )

    interface OnBillOptionListener {
        fun onCloseTask()
        fun onReOpenTask()
    }

    private var mMoreTaskPopup: PopupWindow? = null
    private var xOffValue = 0

    init {
        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        mMoreTaskPopup = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        mMoreTaskPopup!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mMoreTaskPopup!!.isOutsideTouchable = true
//        setLayout(contentView, checkShaped)
        xOffValue = binding.root.measuredWidth

        binding.tvClose.setOnClickListener {
            mMoreTaskPopup?.dismiss()
            onItemClick.onCloseTask()
        }

        binding.tvReOpen.setOnClickListener {
            mMoreTaskPopup?.dismiss()
            onItemClick.onReOpenTask()
        }
    }

    fun showPopupWindow(anchorView: View) {
        mMoreTaskPopup?.width = UIHelper.getScreenWidth(context) / 2
        mMoreTaskPopup?.showAsDropDown(anchorView, -xOffValue + anchorView.height, 0)
    }

    fun onDestroy() {
        mMoreTaskPopup?.dismiss()
        mMoreTaskPopup = null
    }
}