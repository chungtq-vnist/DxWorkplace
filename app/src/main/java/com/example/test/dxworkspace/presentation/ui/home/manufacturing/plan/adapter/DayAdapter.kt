package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.work_schedule.SubCommandInWorkSchedule
import com.example.test.dxworkspace.databinding.ItemDayBinding
import com.example.test.dxworkspace.databinding.ItemShiftInDayBinding
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz

class DayAdapter  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var i : Int = 1 // stt ca
    var data = listOf<SubCommandInWorkSchedule?>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DayViewHolder(ItemDayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(getItemViewType(position) == 0){
            (holder as DayViewHolder).binding.apply {
                tvShift.text = " Ngày "
            }
        } else {
            val item = data[position-1]
            (holder as DayViewHolder).binding.apply {
                tvShift.text = position.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return if(data.isEmpty()) 0 else data.size + 1
    }

}
class DayViewHolder(val binding : ItemDayBinding) : RecyclerView.ViewHolder(binding.root){

}