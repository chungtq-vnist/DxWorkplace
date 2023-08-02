package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.databinding.ItemRecycleProductBinding
import com.example.test.dxworkspace.presentation.utils.common.getTextz

class ChooseProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listgood = listOf<GoodDetailModel>()
    var listInventory = listOf<InventoryGoodWrap>()
    var data = mutableListOf<DataProductInput>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemRecycleProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        (holder as ViewHolder).apply {
            binding.apply {
                val adapter = ArrayAdapter<GoodDetailModel>(
                    root.context,
                    android.R.layout.simple_dropdown_item_1line,
                    listgood
                )
                edtLineCost.removeTextChangedListener(holder.textWatcher)
                edtLineCost.setText(item.quantity.toString())
                edtVariantName.setAdapter(adapter)
                edtVariantName.onItemClickListener =
                    AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                        item.goodId = (p0.getItemAtPosition(p2) as GoodDetailModel)._id
                        println("POS1 : ${p2}")
                        notifyItemChanged(position)
                    }
                edtLineCost.addTextChangedListener(holder.textWatcher)
//                edtLineCost.doAfterTextChanged {
//                    item.quantity =
//                        if (edtLineCost.getTextz().isEmpty()) 0 else edtLineCost.getTextz().toInt()
//                }
                if (item.goodId != "")
                    justTry {
                        println("POS2 : ${listgood.indexOfFirst { it._id == item.goodId }}")
//                        edtVariantName.setSelection(listgood.indexOfFirst { it._id == item.goodId })
                        edtVariantName.setText(listgood.find { it._id == item.goodId }?.toString(),false)

                    }
                else edtVariantName.setText("")
                edtPriceType.setText(if (item.goodId == "") "" else listgood.find { it._id == item.goodId }?.baseUnit)
                edtPriceInit.setText(
                    if (item.goodId == "") "0" else listInventory.find { it.good._id == item.goodId }?.inventory.toString()
                        ?: "0"
                )
                if (itemCount == 1) {
                    frameTop.isVisible = false
                } else {
                    frameTop.isVisible = true
                }
                ivDeleteVariant.setOnClickListener {
                    data.removeAt(position)
                    notifyDataSetChanged()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

   inner class ViewHolder(val binding: ItemRecycleProductBinding) : RecyclerView.ViewHolder(binding.root) {
        var textWatcher : TextWatcher? = null
        init {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    TODO("Not yet implemented")
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    TODO("Not yet implemented")
                }

                override fun afterTextChanged(p0: Editable?) {
                    data.get(adapterPosition).quantity =
                        if (binding.edtLineCost.getTextz().isEmpty()) 0 else binding.edtLineCost.getTextz().toInt()
                }
            }

        }

    }

}


data class DataProductInput(
    var quantity: Int = 0,
    var goodId: String = ""
)