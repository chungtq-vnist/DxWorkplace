package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.databinding.ItemActionTimesheetBinding

class TaskActionTimeSheetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : MutableList<ActionTimeSheet> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var onClick :((ActionTimeSheet) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemActionTimesheetBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as ViewHolder).bind(item)
        (holder as ViewHolder).binding.root.setOnClickListener {
            items.forEach {
                it.isSelected = it.id == item.id
            }
            onClick?.invoke(item)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size
}

class ViewHolder(val binding : ItemActionTimesheetBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : ActionTimeSheet){
        binding.apply {
            tvAction.text  = item.name
            cbSelected.isChecked = item.isSelected
        }
    }

}

data class ActionTimeSheet(
    var id : String ,
    var isSelected : Boolean = false,
    var  name : String
)