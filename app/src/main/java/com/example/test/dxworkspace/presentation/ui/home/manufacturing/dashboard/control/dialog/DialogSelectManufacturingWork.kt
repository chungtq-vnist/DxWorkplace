package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.databinding.BottomDialogSelectManufacturingworkBinding
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter.ManufacturingWorkSelectAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus

class DialogSelectManufacturingWork() : BottomSheetDialogFragment() {

    var onApplyWorks: ((MutableList<ManufacturingWorkSelect>) -> Unit)? = null

    private lateinit var binding: BottomDialogSelectManufacturingworkBinding
    private val adapter by lazy { ManufacturingWorkSelectAdapter() }


    var data = mutableListOf<ManufacturingWorkSelect>()
    set(value) {
        field = value
    }

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
        binding = BottomDialogSelectManufacturingworkBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupUI() {

        binding.ivClose.setOnClickListener {
            dismiss()

        }
        binding.ivApply.setOnClickListener {
            val t = adapter.items
            onApplyWorks?.invoke(t)
            dismiss()
        }
        binding.cbAll.isChecked = data.find { it.isSelected } == null
//        val layoutManager = MaxItemLayoutManager(context)
//        layoutManager.maxItem = 7
//        binding.rclv.layoutManager = layoutManager
        binding.rclv.adapter = adapter
        adapter.items = data
        adapter.onSelect = {
            binding.cbAll.isChecked = it
        }
        binding.lnAll.setOnClickListener {
            binding?.cbAll.isChecked = !binding.cbAll.isChecked
            data.forEach { it.isSelected = binding.cbAll.isChecked }
            adapter.notifyDataSetChanged()
        }
        binding.rclv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }
}