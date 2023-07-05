package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.ItemSelectManufacturingWorkBinding

class SelectUsersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<UserProfileResponse>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listChoose = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(ItemSelectManufacturingWorkBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val i = items[position]
        (holder as UserViewHolder).apply {
            binding.apply {
                tvName.text = i.name + " ( ${i.email} )"
                cbSelected.isChecked = listChoose.contains(i.id)
                root.setOnClickListener {
                    cbSelected.isChecked = !cbSelected.isChecked
                    if(cbSelected.isChecked) listChoose.add(i.id) else listChoose.remove(i.id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class UserViewHolder(val binding : ItemSelectManufacturingWorkBinding) : RecyclerView.ViewHolder(binding.root){

}