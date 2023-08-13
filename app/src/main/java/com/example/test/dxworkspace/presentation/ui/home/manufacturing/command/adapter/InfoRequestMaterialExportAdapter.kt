package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.bill.SubGoodsBill
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.databinding.ItemInfoRequestExportMaterialBinding
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz

class InfoRequestMaterialExportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<ProductRequestManagementModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestViewHolder(
            ItemInfoRequestExportMaterialBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as RequestViewHolder).apply {
            binding.apply {
                edtStock.setText(item.code)
                when (item.status) {
                    1 -> {
                        edtBillStatus.setText("Chờ phê duyệt")
                        edtBillStatus.setTextColorz(R.color.clr_status_wait)
                    }
                    2 -> {
                        edtBillStatus.setText("Yêu cầu đã gửi đến kho")
                        edtBillStatus.setTextColorz(R.color.clr_wait_confirm_grab)
                    }
                    3 -> {
                        edtBillStatus.setText("Đã phê duyệt yêu cầu xuất kho")
                        edtBillStatus.setTextColorz(R.color.clr_status_approve)
                    }
                    4 -> {
                        edtBillStatus.setText("Đang tiến hành xuất kho")
                        edtBillStatus.setTextColorz(R.color.clr_status_inprogress)
                    }
                    5 -> {
                        edtBillStatus.setText("Đã hoàn thành xuất kho")
                        edtBillStatus.setTextColorz(R.color.clr_status_finish)
                    }
                    6 -> {
                        edtBillStatus.setText("Đã hủy yêu cầu xuất kho")
                        edtBillStatus.setTextColorz(R.color.clr_status_cancel)
                    }
                }
                val adapter = InfoMaterialInBillAdapter()
                rcvItem.adapter = adapter
                adapter.items = item.goods ?: listOf()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class RequestViewHolder(val binding: ItemInfoRequestExportMaterialBinding) : RecyclerView.ViewHolder(
    binding.root
) {

}