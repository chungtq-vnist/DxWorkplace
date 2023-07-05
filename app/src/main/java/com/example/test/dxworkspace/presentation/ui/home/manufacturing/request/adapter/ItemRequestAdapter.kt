package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.databinding.ItemRecycleCreateRequestBinding

class ItemRequestAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<GoodDetailModel>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemRecycleCreateRequestBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val t = items[position]
        (holder as ViewHolder).binding.apply {
            edtVariantName.setText(t.name)
            edtCode.setText(t.code)
            edtPriceInit.setText(t.baseUnit)
            edtLineCost.setText(t.quantity.toString())
        }
    }

    override fun getItemCount(): Int = items.size
}
class ViewHolder(val binding : ItemRecycleCreateRequestBinding) : RecyclerView.ViewHolder(binding.root){}