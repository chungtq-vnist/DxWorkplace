package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.bill.BillByCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.databinding.BottomDialogOptionCommandBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialogOptionCommand(val command : ManufacturingCommandModel ,val listBill : List<BillByCommandModel> ,val config : ConfigRepository) : BottomSheetDialogFragment() {
    lateinit var binding : BottomDialogOptionCommandBinding

    var onExport : (() -> Unit)? = null
    var onStart : (() -> Unit)? = null
    var onFinish : (() -> Unit)? = null
    var onCancel : (() -> Unit)? = null
    var onBuyGood : (() -> Unit)? = null
    var onCheckQuality : (() -> Unit)? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        println("onCreateDialog")
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                        as? FrameLayout
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("onCreate")
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView")
        binding = BottomDialogOptionCommandBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    fun setupUI(){
        val userId = config.getUser().id
        binding.apply {
            tvBuyGood.isVisible = command.approvers?.map { it.approver?._id }?.contains(userId) == true && command.status == 1
            tvExportGood.isVisible = command.approvers?.map { it.approver?._id }?.contains(userId) == true  && listBill.isEmpty() && command.status == 1
            tvStart.isVisible = command.accountables?.map { it._id }?.contains(userId) == true && (command.status == 1|| command.status == 2)
            tvFinish.isVisible = command.accountables?.map { it._id }?.contains(userId) == true && (command.status == 3)
            tvQuality.isVisible = command.qualityControlStaffs?.map { it.staff._id }?.contains(userId) == true && (command.status == 3 || command.status == 4)
            tvCancel.isVisible = command.status != 5

            tvBuyGood.setOnClickListener {
                onBuyGood?.invoke()
                dismiss()
            }
            tvExportGood.setOnClickListener {
                onExport?.invoke()
                dismiss()
            }
            tvStart.setOnClickListener {
                onStart?.invoke()
                dismiss()
            }
            tvFinish.setOnClickListener {
                onFinish?.invoke()
                dismiss()
            }
            tvCancel.setOnClickListener {
                onCancel?.invoke()
                dismiss()
            }
            tvQuality.setOnClickListener {
                onCheckQuality?.invoke()
                dismiss()
            }
        }
    }
}