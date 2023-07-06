package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubMaterialModel
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.ParamGood
import com.example.test.dxworkspace.databinding.ItemRecycleExportBillBinding
import com.example.test.dxworkspace.presentation.model.menu.Good

class InfoMaterialExportNewAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listMaterial = listOf<SubMaterialModel>()
    var items = mutableListOf<ParamGood>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onDelete :((pos :Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoMaterialExportViewHolder(
            ItemRecycleExportBillBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as InfoMaterialExportViewHolder).binding.apply {
            tvDelete.text = "Xóa NVL"
            tvEdit.isVisible  = false
            tilBillCode.hint = "Nguyên vật liệu"
            edtBillCode.setText(listMaterial.find{it.good?._id == item.good}.toString())
            tilCommandCode.isVisible = false
            tilStock.hint = "Số lượng"
            edtStock.setText(item.quantity)
            tvDelete.setOnClickListener { onDelete?.invoke(position) }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class InfoMaterialExportViewHolder( val binding : ItemRecycleExportBillBinding) : RecyclerView.ViewHolder(binding.root){


}