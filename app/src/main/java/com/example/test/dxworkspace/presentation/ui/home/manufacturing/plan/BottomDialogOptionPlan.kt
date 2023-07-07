package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanDetailModel
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.databinding.BottomDialogOptionPlanBinding
import com.example.test.dxworkspace.databinding.BottomDialogOptionRequestBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialogOptionPlan(val plan : ManufacturingPlanDetailModel, val config : ConfigRepository) : BottomSheetDialogFragment() {
    lateinit var binding : BottomDialogOptionPlanBinding


    var onConfirm : (() -> Unit)? = null
    var onCancel : (() -> Unit)? = null



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
        binding = BottomDialogOptionPlanBinding.inflate(inflater, container, false)
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
            tvConfirm.isVisible = plan.status == 1
            tvCancel.isVisible = true
            tvCancel.setOnClickListener {
                onCancel?.invoke()
                dismiss()
            }
            tvConfirm.setOnClickListener {
                onConfirm?.invoke()
                dismiss()
            }
        }
    }
}