package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubMaterialModel
import com.example.test.dxworkspace.databinding.ItemMaterialInfoBinding

class InfoMaterialAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var items = listOf<SubMaterialModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listInventory = listOf<InventoryGoodWrap>()
    var quantity = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoMaterialViewHolder(ItemMaterialInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as InfoMaterialViewHolder).binding.apply {
            edtCode.setText(item.good?.code)
            edtVariantName.setText(item.good?.name)
            edtPriceType.setText(item.good?.baseUnit)
            val t = listInventory.find { it.good._id == item.good?._id }
            edtPriceInit.setText(t?.inventory.toString() )
            val e = quantity*(item.quantity ?: 0)
            edtLineCost.setText(e.toString())
            edtStatus.setText(if(t?.inventory ?: 0 >= e) "Đủ" else "Thiếu")
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
}

class InfoMaterialViewHolder(val binding : ItemMaterialInfoBinding) : RecyclerView.ViewHolder(binding.root){

}