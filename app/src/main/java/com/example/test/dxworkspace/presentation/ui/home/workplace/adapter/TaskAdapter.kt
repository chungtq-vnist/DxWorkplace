package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.task.TaskModel
import com.example.test.dxworkspace.databinding.ItemTaskBinding
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromString
import com.example.test.dxworkspace.presentation.utils.getTimeInMillisFromString

class TaskAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items : MutableList<TaskModel> = mutableListOf()
        set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onClick :((TaskModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as TaskViewHolder).apply {
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

class TaskViewHolder(val binding : ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : TaskModel){
        binding.apply {
//            viewStatus.setBackgroundColor(if(item.status == "finished") viewStatus.context.getColor(R.color.clr_green) else viewStatus.context.getColor(R.color.red) )
            viewStatus.setBackgroundColor(
                when (item.priority) {
                    1 -> {
                        viewStatus.context.getColor(R.color.clr_green)
                    }
                    2 -> {
                        viewStatus.context.getColor(R.color.rajah)
                    }
                    3 -> {

                        viewStatus.context.getColor(R.color.azure_radiance)
                    }
                    4 -> {
                        viewStatus.context.getColor(R.color.clr_cancel_kitchen)
                    }
                    5 -> {
                        viewStatus.context.getColor(R.color.color_item_delivery)
                    }
                    else -> {
                        viewStatus.context.getColor(R.color.clr_green)
                    }
                }
            )
            tvTaskName.text = item.name
            tvTimeEnd.text = getTimeDDMMYYYYHHMMFromString(item.endDate)
        }
    }


}