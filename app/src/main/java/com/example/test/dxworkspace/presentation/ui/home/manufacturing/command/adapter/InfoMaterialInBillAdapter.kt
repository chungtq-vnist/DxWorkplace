package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.bill.SubGoodsBill
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubMaterialModel
import com.example.test.dxworkspace.databinding.ItemMaterialInfoBillExportBinding
import com.example.test.dxworkspace.databinding.ItemMaterialInfoBinding

class InfoMaterialInBillAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var items = listOf<SubGoodsBill>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listInventory = listOf<InventoryGoodWrap>()
    var quantity = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoBillExport(
            ItemMaterialInfoBillExportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as InfoBillExport).binding.apply {
            edtCode.setText(item.good?.code)
            edtVariantName.setText(item.good?.name)
            edtQuantity.setText(item.quantity.toString())
            edtUnit.setText(item.good?.baseUnit)
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
}
class InfoBillExport( val binding : ItemMaterialInfoBillExportBinding) : RecyclerView.ViewHolder(binding.root)