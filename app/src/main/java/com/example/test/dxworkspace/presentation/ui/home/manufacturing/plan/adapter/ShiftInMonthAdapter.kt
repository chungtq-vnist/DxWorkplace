package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.work_schedule.SubCommandInWorkSchedule
import com.example.test.dxworkspace.databinding.ItemShiftInMonthBinding

class ShiftInMonthAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var code : String =""
    var data = listOf<List<SubCommandInWorkSchedule?>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onChooseShift2 : ((shift:Int , day : Int,check : Boolean) -> Unit)? = null
    var onViewDetailShift : ((shift:Int , day : Int) -> Unit)? = null


    override fun getItemViewType(position: Int): Int {
        return if(position == 0 ) 0 else 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShiftInMonthViewHolder(ItemShiftInMonthBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = if(position == 0) data[0] else  data[position-1]
        (holder as ShiftInMonthViewHolder).binding.apply {
            if (position == 0) {
                val adapter = DayAdapter()
                rcvShitInDay.adapter = adapter
                adapter.data = item
            } else {
                val adapter = ShiftInDayAdapter()
                adapter.code = code
                adapter.i = position
                rcvShitInDay.adapter = adapter
                adapter.data = item.toMutableList()
                adapter.onChooseShift = { s ,d ,c ->
                    onChooseShift2?.invoke(s,d,c)
                }
                adapter.onViewDetail = {s,d ->
                    onViewDetailShift?.invoke(s,d)

                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if(data.size == 0 ) 0 else  data.size +1
    }
}
class ShiftInMonthViewHolder( val binding : ItemShiftInMonthBinding) : RecyclerView.ViewHolder(binding.root){
//    fun bind(item : List<SubCommandInWorkSchedule?> ,position: Int){
//        if(position == 0){
//            val adapter = DayAdapter()
//            binding.rcvShitInDay.adapter = adapter
//            adapter.data = item
//        } else {
//        val adapter = ShiftInDayAdapter()
//        adapter.i = position
//        binding.rcvShitInDay.adapter = adapter
//        adapter.data = item
//        }
//    }
}