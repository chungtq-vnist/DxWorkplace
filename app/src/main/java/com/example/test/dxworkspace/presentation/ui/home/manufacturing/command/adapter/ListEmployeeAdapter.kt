package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.databinding.ItemEmployeeBinding

class ListEmployeeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: List<SubUserBasicModel> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EmployeeViewHolder(
            ItemEmployeeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val taskAction = items[position]
        (holder as EmployeeViewHolder).apply {
            bind(taskAction)
        }
    }

    override fun getItemCount(): Int = items.size
}

class EmployeeViewHolder(val binding : ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : SubUserBasicModel){
        binding.apply {
            tvUserName.text = item.name + "- "+ item.email
        }
    }
}