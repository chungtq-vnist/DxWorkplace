package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.manufacturing_plan.SubGoodInPlan
import com.example.test.dxworkspace.databinding.ItemRecycleProductInPlanDetailBinding

class ProductInPlanDetailAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<SubGoodInPlan>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            ItemRecycleProductInPlanDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val i = items[position]
        (holder as ProductViewHolder).binding.apply {
            edtCode.setText(i.good?.code)
            edtName.setText(i.good?.name)
            edtQuantity.setText(i.quantity.toString())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ProductViewHolder(val binding : ItemRecycleProductInPlanDetailBinding) : RecyclerView.ViewHolder(binding.root){}