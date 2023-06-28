package com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.databinding.ItemManufacturingWorkBinding

class ManufacturingMillAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : List<ManufacturingMillModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick :((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MillViewHolder(ItemManufacturingWorkBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as MillViewHolder).apply {
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
class MillViewHolder(val binding : ItemManufacturingWorkBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ManufacturingMillModel){
        binding.apply {
            tvName.text = item.name
            tvCode.text = item.code
            ivStatus.setImageResource(if(item.status == 1) R.drawable.ic_compelete else R.drawable.ic_cancel)
        }
    }


}