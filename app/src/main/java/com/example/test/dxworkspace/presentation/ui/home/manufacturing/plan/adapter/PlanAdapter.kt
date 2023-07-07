package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.databinding.ItemManufacturingCommandBinding
import com.example.test.dxworkspace.databinding.ItemManufacturingPlanBinding
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY

class PlanAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : List<ManufacturingPlanModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick :((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlanViewHolder(ItemManufacturingPlanBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as PlanViewHolder).apply {
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
class PlanViewHolder(val binding : ItemManufacturingPlanBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ManufacturingPlanModel){
        binding.apply {
            tvName.text = item.code
            tvName2.text = getddMMYYYY(item.createdAt)
            when(item.status){
                1 -> {
                    tvStatus.text = "Chờ phê duyệt"
                    tvStatus.setTextColorz(R.color.clr_status_wait)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_wait)
                }
                2 -> {
                    tvStatus.text = "Đã phê duyệt"
                    tvStatus.setTextColorz(R.color.clr_status_approve)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_approve)
                }
                3 -> {
                    tvStatus.text = "Đang thực hiện"
                    tvStatus.setTextColorz(R.color.clr_status_inprogress)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_inprogress)
                }
                4 -> {
                    tvStatus.text = "Đã hoàn thành"
                    tvStatus.setTextColorz(R.color.clr_status_finish)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_finish)
                }
                5 -> {
                    tvStatus.text = "Đã hủy"
                    tvStatus.setTextColorz(R.color.clr_status_cancel)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_cancel)
                }
                else -> {
                    tvStatus.text = "Error"
                    tvStatus.setTextColorz(R.color.clr_status_cancel)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_cancel)
                }
            }

        }
    }


}