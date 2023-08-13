package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.databinding.ItemManufacturingRequestBinding
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY

class RequestManagementAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : List<ProductRequestManagementModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClick :((ProductRequestManagementModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestViewHolder(ItemManufacturingRequestBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as RequestViewHolder).binding.apply {
            tvName.text = item.code
            tvName2.text = getddMMYYYY(item.createdAt)
            root.setOnClickListener { onClick?.invoke(item) }
            when(item.type){
                1 -> {
                    when(item.status) {
                        1 -> {
                            tvStatus.text = "Chờ phê duyệt"
                            tvStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        2 -> {
                            tvStatus.text = "Yêu cầu đã gửi đến bộ phận mua hàng"
                            tvStatus.setTextColorz(R.color.clr_wait_confirm_grab)
                        }
                        3 -> {
                            tvStatus.text = "đã phê duyệt mua hàng"
                            tvStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        4 -> {
                            tvStatus.text = "Đã tạo đơn mua hàng"
                            tvStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        5 -> {
                            tvStatus.text = "Chờ phê duyệt yêu cầu"
                            tvStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        6 -> {
                            tvStatus.text = "Đã gửi yêu cầu nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        7 -> {
                            tvStatus.text = "Đã phê duyệt yêu cầu nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        8 -> {
                            tvStatus.text = "Đang tiến hành nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_inprogress)
                        }
                        9 -> {
                            tvStatus.text = "Đã hoàn thành nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_finish)
                        }
                        10 -> {
                            tvStatus.text = "Đã hủy yêu cầu mua hàng"
                            tvStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                        11 -> {
                            tvStatus.text = "Đã hủy yêu cầu nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                    }
                }
                2 -> {
                    when (item.status) {
                        1 -> {
                            tvStatus.text = "Chờ phê duyệt"
                            tvStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        2 -> {
                            tvStatus.text = "Yêu cầu đã gửi đến kho"
                            tvStatus.setTextColorz(R.color.clr_wait_confirm_grab)
                        }
                        3 -> {
                            tvStatus.text = "Đã phê duyệt yêu cầu nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        4 -> {
                            tvStatus.text = "Đang tiến hành nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_inprogress)
                        }
                        5 -> {
                            tvStatus.text = "Đã hoàn thành nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_finish)
                        }
                        6 -> {
                            tvStatus.text = "Đã hủy yêu cầu nhập kho"
                            tvStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                    }
                }
                3 -> {
                    when (item.status) {
                        1 -> {
                            tvStatus.text = "Chờ phê duyệt"
                            tvStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        2 -> {
                            tvStatus.text = "Yêu cầu đã gửi đến kho"
                            tvStatus.setTextColorz(R.color.clr_wait_confirm_grab)
                        }
                        3 -> {
                            tvStatus.text = "Đã phê duyệt yêu cầu xuất kho"
                            tvStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        4 -> {
                            tvStatus.text = "Đang tiến hành xuất kho"
                            tvStatus.setTextColorz(R.color.clr_status_inprogress)
                        }
                        5 -> {
                            tvStatus.text = "Đã hoàn thành xuất kho"
                            tvStatus.setTextColorz(R.color.clr_status_finish)
                        }
                        6 -> {
                            tvStatus.text = "Đã hủy yêu cầu xuất kho"
                            tvStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int =
        items.size

}

class RequestViewHolder( val binding : ItemManufacturingRequestBinding) : RecyclerView.ViewHolder(binding.root){

}