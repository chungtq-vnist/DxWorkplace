package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.login.RoleResponse
import com.example.test.dxworkspace.databinding.ItemActionTimesheetBinding

class SelectRoleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : MutableList<RoleResponse> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var roleNow = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoleViewHoler(
            ItemActionTimesheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as RoleViewHoler).binding.apply {
            tvAction.text = item.name
            cbSelected.isChecked = item.id == roleNow
        }
        (holder as RoleViewHoler).binding.root.setOnClickListener {
            roleNow = item.id
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size
}

class RoleViewHoler(val binding : ItemActionTimesheetBinding) : RecyclerView.ViewHolder(binding.root){
}