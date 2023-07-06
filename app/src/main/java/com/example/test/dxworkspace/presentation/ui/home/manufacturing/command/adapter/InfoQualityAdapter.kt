package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.data.entity.bill.SubGoodsBill
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubQualityControlStaff
import com.example.test.dxworkspace.databinding.ItemMaterialInfoBillExportBinding
import com.example.test.dxworkspace.databinding.ItemQuanlityControlBinding
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromStringOneLine

class InfoQualityAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var items = listOf<SubQualityControlStaff>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listInventory = listOf<InventoryGoodWrap>()
    var quantity = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoQualityViewHolder(
            ItemQuanlityControlBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as InfoQualityViewHolder).binding.apply {
            edtName.setText(item.staff.name)
            edtStatus.setText(when(item.status){
                1 -> "Chưa kiểm định"
                2 -> "Đạt kiểm định"
                3 -> "Không đạt kiểm định"
                else ->"Chưa kiểm định"
            })
            edtStatus.setTextColorz(
                when(item.status){
                1 -> R.color.clr_status_wait
                2 -> R.color.clr_status_finish
                3 -> R.color.clr_status_cancel
                else -> R.color.clr_status_wait
            })
            justTry {  edtTime.setText(getTimeDDMMYYYYHHMMFromStringOneLine(item.time?:"")) }
            edtContentQuality.setText(item.content)
            edtContentQuality.isVisible = item.status != 1
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
}
class InfoQualityViewHolder(val binding : ItemQuanlityControlBinding) : RecyclerView.ViewHolder(binding.root){}