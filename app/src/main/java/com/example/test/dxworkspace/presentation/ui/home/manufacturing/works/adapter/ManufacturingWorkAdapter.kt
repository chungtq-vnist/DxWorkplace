package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.task.TaskModel
import com.example.test.dxworkspace.databinding.ItemManufacturingWorkBinding
import com.example.test.dxworkspace.databinding.ItemTaskBinding
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.TaskViewHolder
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromString

class ManufacturingWorkAdapter  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : MutableList<ManufacturingWorkEntity> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick :((ManufacturingWorkEntity) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkViewHolder(ItemManufacturingWorkBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as WorkViewHolder).apply {
            bind(item)
            binding.root.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
class WorkViewHolder(val binding : ItemManufacturingWorkBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ManufacturingWorkEntity){
        binding.apply {
            tvName.text = item.name
            tvCode.text = item.code
            ivStatus.setImageResource(if(item.status == 1) R.drawable.ic_compelete else R.drawable.ic_cancel)
        }
    }


}