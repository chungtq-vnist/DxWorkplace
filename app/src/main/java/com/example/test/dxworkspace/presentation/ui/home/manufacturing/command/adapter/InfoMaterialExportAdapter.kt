package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.databinding.ItemRecycleExportBillBinding
import com.example.test.dxworkspace.presentation.model.menu.Good

class InfoMaterialExportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<Good>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemRecycleExportBillBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as ViewHolder).binding.apply {
            holder.bind(item)

        }
    }

    override fun getItemCount(): Int = items.size

}

class ViewHolder( val binding : ItemRecycleExportBillBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : Good){
        binding.apply {
            tvDelete.text = "Xóa NVL"
            tilBillCode.hint = "Mã NVL"
            edtBillCode.setText(item.code)
            tilCommandCode.isVisible = false
            tilStock.hint = "Số lượng"
            edtStock.setText(item.quantity.toString())
        }
    }

}