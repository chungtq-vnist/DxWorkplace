package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailModel
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.databinding.ItemRecycleImportBillBinding
import com.example.test.dxworkspace.presentation.model.menu.BillExportMaterialRequest
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz

class InfoBillImportNewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listStock = listOf<StockModel>()
    var lot = ManufacturingLotDetailModel()
    var onDelete : ((pos:Int) -> Unit)? = null
    var onEdit : ((pos:Int) -> Unit)? = null
    var items = mutableListOf<ParamCreateProductRequest>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BillImportViewHolder(
            ItemRecycleImportBillBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as BillImportViewHolder).binding.apply {
            edtBillCode.setText(item.code)
            tilBillCode.setHint("Mã đề nghị")
            tvDelete.setText("Xóa đề nghị")
            val t = lot.good?.name + "-" + lot.good?.code
            edtCommandCode.setText(t)
            edtQuantity.setText(item.goods?.firstOrNull()?.quantity.toString())
            edtStock.setText(listStock.find { it._id == item.stock }?.name)
            tvEdit.setTextColorz(R.color.clr_orange)
            tvEdit.setOnClickListener {
                onEdit?.invoke(position)
            }
            tvDelete.setOnClickListener {
                onDelete?.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

}