package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.role.RoleEntity
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.databinding.ItemSelectManufacturingWorkBinding
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect

class RolesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<RoleModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listChoose = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemSelectManufacturingWorkBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val i = items[position]
        (holder as ViewHolder).apply {
            binding.apply {
                tvName.text = i.name
                cbSelected.isChecked = listChoose.contains(i._id)
                root.setOnClickListener {
                    cbSelected.isChecked = !cbSelected.isChecked
                    if(cbSelected.isChecked) listChoose.add(i._id) else listChoose.remove(i._id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(val binding : ItemSelectManufacturingWorkBinding) : RecyclerView.ViewHolder(binding.root){

}