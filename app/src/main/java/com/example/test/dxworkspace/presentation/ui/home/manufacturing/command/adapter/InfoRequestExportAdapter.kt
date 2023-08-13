package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.databinding.ItemRecycleExportRequestBinding
import com.example.test.dxworkspace.presentation.model.menu.BillExportMaterialRequest

class InfoRequestExportAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listStock = listOf<StockModel>()
    var items = mutableListOf<ParamCreateProductRequest>()
        set(value) {
            field = value
            notifyDataSetChanged()

        }
    var onEdit : ((pos:Int) -> Unit)? = null
    var onDelete : ((pos:Int) -> Unit)? = null
    var onChange : ((isEmpty : Boolean) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoRequestViewHolder(
            ItemRecycleExportRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as InfoRequestViewHolder).binding.apply {
            edtBillCode.setText(item.code)
            val stock = listStock.find { it._id == item.stock }
            edtCommandCode.setText(stock?.name)
            tvEdit.setOnClickListener { onEdit?.invoke(position) }
            tvDelete.setOnClickListener { onDelete?.invoke(position) }
        }
    }

    override fun getItemCount(): Int {
        onChange?.invoke(items.isEmpty())
        return items.size
    }

}

class InfoRequestViewHolder (val binding : ItemRecycleExportRequestBinding) : RecyclerView.ViewHolder(binding.root){

}