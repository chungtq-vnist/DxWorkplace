package com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.DialogPickerMonthYearBinding
import com.example.test.dxworkspace.presentation.utils.common.initDialog

class MonthYearPickerDialog(context: Context, month: Int, year : Int ) {
    private var binding : DialogPickerMonthYearBinding? = null
    var dialog: AlertDialog? = null
    var onClick: ((Int, Int) -> Unit)? = null

    var m = 1
    var y = 2023
    init {
        val builder =AlertDialog.Builder(context)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_picker_month_year,
        ConstraintLayout(context),false)
        builder.setView(binding!!.root)
        m = month
        y = year
        dialog = builder.create()
        dialog?.initDialog(true)
        binding?.apply {
            tvDone.setOnClickListener {
                onClick?.invoke(m,y)
                dialog?.dismiss()
            }
            pickerMonth.minValue = 1
            pickerMonth.maxValue = 12
            pickerYear.minValue = 1970
            pickerYear.maxValue = 2100
            tvTimeSelect.text = "${m} - ${y}"
            pickerMonth.value = m
            pickerYear.value = y
            pickerMonth.setOnValueChangedListener { numberPicker, i, i2 ->
                m = i2
                tvTimeSelect.text = "${m} - ${y}"
            }
            pickerYear.setOnValueChangedListener { numberPicker, i, i2 ->
                y = i2
                tvTimeSelect.text = "${m} - ${y}"
            }
        }
    }
    fun onDestroy() {
        dialog = null
        dialog?.dismiss()
    }

    fun show() {
        dialog?.show()
    }

}