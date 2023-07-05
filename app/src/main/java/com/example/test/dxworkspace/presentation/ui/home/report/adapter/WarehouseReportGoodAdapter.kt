package com.example.test.dxworkspace.presentation.ui.home.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.report.ItemInWarehouseReport
import com.example.test.dxworkspace.databinding.ItemDetailGoodWarehouseBinding
import com.example.test.dxworkspace.presentation.model.menu.CompareModel

class WarehouseReportGoodAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<ItemInWarehouseReport>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GoodViewHolder(ItemDetailGoodWarehouseBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as GoodViewHolder).apply {
            bind(item)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class GoodViewHolder(val binding : ItemDetailGoodWarehouseBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ItemInWarehouseReport){
        binding.apply {
            tvRevenueTitle.text = item.good.name
            tvQuantityBegin.text = item.beginningQuantity.toString()
            tvQuantityEnd.text = item.endingQuantity.toString()
        }
    }
}