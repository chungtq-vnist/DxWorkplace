package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailModel
import com.example.test.dxworkspace.databinding.ItemRecycleImportBillBinding
import com.example.test.dxworkspace.presentation.model.menu.BillExportMaterialRequest

class InfoBillImportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listStock = listOf<StockModel>()
    var lot = ManufacturingLotDetailModel()
    var items = mutableListOf<BillExportMaterialRequest>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BillImportViewHolder(
            ItemRecycleImportBillBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as BillImportViewHolder).binding.apply {
            edtBillCode.setText(item.code)
            val t = lot.good?.name + "-" + lot.good?.code
            edtCommandCode.setText(t)
            edtQuantity.setText(item.goods.firstOrNull()?.quantity.toString())
            edtStock.setText(listStock.find { it._id == item.fromStock }?.name)
        }
    }

    override fun getItemCount(): Int = items.size

}
class BillImportViewHolder(val binding : ItemRecycleImportBillBinding) : RecyclerView.ViewHolder(binding.root){

}