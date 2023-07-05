package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.databinding.ItemRecycleProductBinding
import com.example.test.dxworkspace.databinding.ItemRecycleProductivityBinding
import com.example.test.dxworkspace.presentation.utils.common.getTextz

class InfoProductivityAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = listOf<GoodDetailModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductivityViewHolder(
            ItemRecycleProductivityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        (holder as ProductivityViewHolder).apply {
            binding.apply {
                edtVariantName.setText(item.name)
                edtCode.setText(item.code)
                edtMill.setText(item.manufacturingMills?.first()?.manufacturingMill?.name)
                edtPriceInit.setText(item.manufacturingMills?.first()?.productivity.toString())
                edtLineCost.setText(item.manufacturingMills?.first()?.personNumber.toString())
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class ProductivityViewHolder(val binding : ItemRecycleProductivityBinding) : RecyclerView.ViewHolder(binding.root){

}