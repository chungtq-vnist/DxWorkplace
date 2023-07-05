package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.databinding.ItemManufacturingCommandBinding
import com.example.test.dxworkspace.databinding.ItemManufacturingWorkBinding
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz

class CommandAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : List<ManufacturingCommandModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick :((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommandViewHolder(ItemManufacturingCommandBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as CommandViewHolder).apply {
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
class CommandViewHolder(val binding : ItemManufacturingCommandBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ManufacturingCommandModel){
        binding.apply {
            tvName.text = item.code
            tvName2.text = item.manufacturingPlan?.code
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
                6 -> {
                    tvStatus.text = "Mới tạo"
                    tvStatus.setTextColorz(R.color.clr_status_wait)
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_wait)
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