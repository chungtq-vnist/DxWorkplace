package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.product_request.GoodInfomation
import com.example.test.dxworkspace.databinding.ItemRecycleProductCreateCommandBinding
import com.example.test.dxworkspace.databinding.ItemRecycleRecommendProductCreatePlanBinding

class RecommendProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listInventory = listOf<InventoryGoodWrap>()
    var data = listOf<GoodInfomation>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecommendProductViewHolder(
            ItemRecycleRecommendProductCreatePlanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        (holder as RecommendProductViewHolder).apply {
            binding.apply {
                justTry {
                    edtVariantName.setText(
                        item.good.code
                    )

                    edtLineCost.setText(item.quantity.toString())
                    edtPriceType.setText(item.good.name)
                    edtPriceInit.setText(
                        if (item.good._id == "") "0" else listInventory.find { it.good._id == item.good._id }?.inventory.toString()
                            ?: "0"
                    )
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class RecommendProductViewHolder(val binding: ItemRecycleRecommendProductCreatePlanBinding) : RecyclerView.ViewHolder(
    binding.root
) {}
