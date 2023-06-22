package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.dxworkspace.BuildConfig
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.di.NetworkModule
import com.example.test.dxworkspace.data.entity.task.Comment
import com.example.test.dxworkspace.data.entity.task.TaskAction
import com.example.test.dxworkspace.data.entity.task.TimeSheetLog
import com.example.test.dxworkspace.databinding.ItemActionTaskBinding
import com.example.test.dxworkspace.databinding.ItemTimesheetLogBinding
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromStringOneLine

class TaskTimeSheetLogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: MutableList<TimeSheetLog> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskTimeSheetViewHolder(ItemTimesheetLogBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val i = items[position]
        (holder as TaskTimeSheetViewHolder).bind(i)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class TaskTimeSheetViewHolder(val binding: ItemTimesheetLogBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TimeSheetLog) {
        binding.apply {
            tvUserName.text = item.creator?.name
            tvTimeSheet.text = getTimeDDMMYYYYHHMMFromStringOneLine(item.startedAt) + " - " + getTimeDDMMYYYYHHMMFromStringOneLine(item.startedAt)
        }
    }
}
