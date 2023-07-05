package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.databinding.ItemRecycleProductCreateCommandBinding

class InfoProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listgood = listOf<GoodDetailModel>()
    var listInventory = listOf<InventoryGoodWrap>()
    var data = listOf<DataInfoProduct>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoProductViewHolder(
            ItemRecycleProductCreateCommandBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        (holder as InfoProductViewHolder).apply {
            binding.apply {
                justTry {
                    edtVariantName.setText(
                        listgood.find { it._id == item.goodId }?.toString()
                    )

                    edtLineCost.setText(item.quantity.toString())
                    edtPriceType.setText(if (item.goodId == "") "" else listgood.find { it._id == item.goodId }?.baseUnit)
                    edtPriceInit.setText(
                        if (item.goodId == "") "0" else listInventory.find { it.good._id == item.goodId }?.inventory.toString()
                            ?: "0"
                    )
                    tvDelete.text = tvDelete.context.getString(R.string.tv_product_need_manufacturing , item.quantityNotOrder.toString())
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class InfoProductViewHolder(val binding: ItemRecycleProductCreateCommandBinding) : RecyclerView.ViewHolder(
    binding.root
) {


}

data class DataInfoProduct(
    var quantity: Int = 0,
    var goodId: String = "",
    var quantityNotOrder: Int = 0
)