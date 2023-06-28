package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.ItemTaskTypeBinding
import com.example.test.dxworkspace.presentation.model.menu.TaskType
import com.example.test.dxworkspace.presentation.utils.common.OnItemClick

class TaskTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<TaskType>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: OnItemClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskTypeViewHolder(
            ItemTaskTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as TaskTypeViewHolder).apply {
            binding.apply {
                tvOrderType.text = item.name
                ivOrderType.setImageResource(item.icon)
                lnOrderType.setBackgroundResource(if (item.isSelected) R.drawable.bg_chip_menu_selected else R.drawable.bg_chip_menu)
                root.setOnClickListener {
                    onItemClick?.onItemClickListener(it,position)
                    items.forEachIndexed { index, orderType ->
                        orderType.isSelected = position == index
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class TaskTypeViewHolder(val binding: ItemTaskTypeBinding) : RecyclerView.ViewHolder(binding.root) {

}