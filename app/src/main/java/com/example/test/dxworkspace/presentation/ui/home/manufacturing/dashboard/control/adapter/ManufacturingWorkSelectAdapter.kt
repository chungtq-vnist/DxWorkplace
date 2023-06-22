package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.databinding.ItemSelectManufacturingWorkBinding
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect

class ManufacturingWorkSelectAdapter : RecyclerView.Adapter<ViewHolder>() {
    var items = mutableListOf<ManufacturingWorkSelect>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var onSelect : ((Boolean) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSelectManufacturingWorkBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        (holder as ViewHolder).apply {
            binding.root.setOnClickListener {
                item.isSelected = !item.isSelected
                notifyItemChanged(position)
                onSelect?.invoke(items.find { f -> !f.isSelected } == null)
            }
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder(val binding : ItemSelectManufacturingWorkBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ManufacturingWorkSelect){
        binding.apply {
            tvName.text = item.name
            cbSelected.isChecked = item.isSelected
        }
    }
}