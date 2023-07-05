package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.databinding.ItemManufacturingCommandBinding
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.CommandViewHolder
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz

class LotAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : List<ManufacturingLotModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick :((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LotViewHolder(
            ItemManufacturingCommandBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as LotViewHolder).apply {
            bind(item)
            binding.root.setOnClickListener {
                onClick?.invoke(item._id)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class LotViewHolder(val binding : ItemManufacturingCommandBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ManufacturingLotModel){
        binding.apply {
            tvName.text = item.code
            tvName2.text = item.good?.name
            when(item.status){
                1 -> {
                    tvStatus.text = "Chưa lên đơn nhập kho"
                    tvStatus.setTextColorz(R.color.clr_status_wait)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_wait)
                }
                2 -> {
                    tvStatus.text = "Đã lên đơn nhập kho"
                    tvStatus.setTextColorz(R.color.clr_status_approve)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_approve)
                }
                3 -> {
                    tvStatus.text = "Đã nhập kho"
                    tvStatus.setTextColorz(R.color.clr_status_inprogress)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_inprogress)
                }
//                4 -> {
//                    tvStatus.text = "Đã hoàn thành"
//                    tvStatus.setTextColorz(R.color.clr_status_finish)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_finish)
//                }
//                5 -> {
//                    tvStatus.text = "Đã hủy"
//                    tvStatus.setTextColorz(R.color.clr_status_cancel)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_cancel)
//                }
//                6 -> {
//                    tvStatus.text = "Mới tạo"
//                    tvStatus.setTextColorz(R.color.clr_status_wait)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_wait)
//                }
                else -> {
                    tvStatus.text = "Error"
                    tvStatus.setTextColorz(R.color.clr_status_cancel)
                }
            }
        }
    }


}