package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.SubGoodInPlan
import com.example.test.dxworkspace.databinding.ItemRecycleCommandInPlanDetailBinding
import com.example.test.dxworkspace.databinding.ItemRecycleProductInPlanDetailBinding
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz

class CommandInPlanDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<ManufacturingCommandModel>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var onClick :((pos:Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommandViewHolder(
            ItemRecycleCommandInPlanDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val i = items[position]
        (holder as CommandViewHolder).binding.apply {
            edtCode.setText(i.code)
            edtName.setText(i.accountables?.map{it.name}?.joinToString(", "))
            edtMill.setText(i.manufacturingMill?.name)
            root.setOnClickListener {
                onClick?.invoke(position)
            }
            when(i.status){
                1 -> {
                    edtStatus.setText("Chờ phê duyệt")
                    edtStatus.setTextColorz(R.color.clr_status_wait)
                }
                2 -> {
                    edtStatus.setText("Đã phê duyệt")
                    edtStatus.setTextColorz(R.color.clr_status_approve)
                }
                3 -> {
                    edtStatus.setText("Đang thực hiện")
                    edtStatus.setTextColorz(R.color.clr_status_inprogress)
                }
                4 -> {
                    edtStatus.setText("Đã hoàn thành")
                    edtStatus.setTextColorz(R.color.clr_status_finish)
                }
                5 -> {
                    edtStatus.setText("Đã hủy")
                    edtStatus.setTextColorz(R.color.clr_status_cancel)
                }
                6 -> {
                    edtStatus.setText("Mới tạo")
                    edtStatus.setTextColorz(R.color.clr_status_wait)
                }
                else -> {
                    edtStatus.setText("Error")
                    edtStatus.setTextColorz(R.color.clr_status_cancel)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
class CommandViewHolder(val binding : ItemRecycleCommandInPlanDetailBinding) : RecyclerView.ViewHolder(binding.root){}