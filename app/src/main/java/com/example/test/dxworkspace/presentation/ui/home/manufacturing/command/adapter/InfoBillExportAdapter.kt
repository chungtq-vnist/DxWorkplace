package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.databinding.ItemRecycleExportBillBinding
import com.example.test.dxworkspace.presentation.model.menu.BillExportMaterialRequest
import com.example.test.dxworkspace.presentation.model.menu.Good

class InfoBillExportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listStock = listOf<StockModel>()
    var command = ManufacturingCommandModel()
    var items = mutableListOf<BillExportMaterialRequest>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BillViewHolder(ItemRecycleExportBillBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as BillViewHolder).binding.apply {
            holder.bind(item)
            edtStock.setText(listStock.find{it._id == item.fromStock}?.name)
            edtCommandCode.setText(command.code)
        }
    }

    override fun getItemCount(): Int = items.size

}

class BillViewHolder( val binding : ItemRecycleExportBillBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : BillExportMaterialRequest){
        binding.apply {
            tvDelete.text = "Xóa Bill"
            tilBillCode.hint = "Mã Bill"
            edtBillCode.setText(item.code)

            tilCommandCode.hint = "Mã lệnh sản xuất"
        }
    }

}