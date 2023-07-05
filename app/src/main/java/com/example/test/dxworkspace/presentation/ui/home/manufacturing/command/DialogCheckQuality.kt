package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.databinding.DialogCheckQualityBinding
import com.example.test.dxworkspace.presentation.model.menu.RequestQualityControl
import com.example.test.dxworkspace.presentation.utils.common.getTextz

class DialogCheckQuality(val command : ManufacturingCommandModel , val userId:String)  : DialogFragment(){
    lateinit var binding : DialogCheckQualityBinding
    var onAccept : ((RequestQualityControl) -> Unit)? = null
    var request = RequestQualityControl()
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DialogCheckQualityBinding.inflate(inflater, container, false)
        return binding.root
    }

        override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSave()
        request.qualityControlStaff.staff = userId
        binding?.apply {
            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
            btnSave.setOnClickListener {
                if(request.qualityControlStaff.status == "0") return@setOnClickListener else {
                    request.qualityControlStaff.content = edtContent.getTextz()
                    onAccept?.invoke(request)
                }
            }
            edtCode.setText(command.code)
            edtQuanlity.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line ,
                listOf("Đạt kiểm định","Không đạt kiểm đinh")))
            edtQuanlity.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as String)
                if(t == "Đạt kiểm định") request.qualityControlStaff.status = "2" else if(t == "Không đạt kiểm đinh")
                    request.qualityControlStaff.status == "3"
                checkSave()
            }

        }
    }

    fun checkSave(){
        if(request.qualityControlStaff.status != "0") binding.btnSave.setBackgroundResource(R.drawable.bg_blue_create) else
            binding.btnSave.setBackgroundResource(R.drawable.bg_border_gray_with_solid)
    }

}