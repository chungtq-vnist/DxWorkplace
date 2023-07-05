package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.databinding.ItemRecycleCreateCommandBinding
import com.example.test.dxworkspace.databinding.ItemRecycleCreateWorkScheduleBinding
import com.example.test.dxworkspace.domain.model.manufacturing_plan.SubRequestCommand

class CreateWorkScheduleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listgood = listOf<GoodDetailModel>()
    var listInventory = listOf<InventoryGoodWrap>()
    var listInfoGood = listOf<DataInfoProduct>()
    var data = listOf<SubRequestCommand>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onSchedule : ((pos:Int , i : SubRequestCommand ,name:String) -> Unit)? = null
    var onRemove : ((pos:Int , i : SubRequestCommand) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkScheduleViewHolder(
            ItemRecycleCreateWorkScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        (holder as WorkScheduleViewHolder).apply {
            binding.apply {
                val good = listgood.find { it._id == item.goodID }
                val context = edtVariantName.context
                edtVariantName.setText(good?.name)
                edtCode.setText(item.code)
                edtPriceInit.setText(item.quantity.toString())
                edtLineCost.setText(if(item.responsibles.isEmpty()) "Chưa phân lịch" else "Đã phân lịch")
                edtLineCost.setOnClickListener {
                    onSchedule?.invoke(position,item,good?.name ?: "")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class WorkScheduleViewHolder(val binding : ItemRecycleCreateWorkScheduleBinding) : RecyclerView.ViewHolder(binding.root){

}