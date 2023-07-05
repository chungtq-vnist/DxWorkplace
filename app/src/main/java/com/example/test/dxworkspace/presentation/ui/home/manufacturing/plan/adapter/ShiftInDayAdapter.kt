package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.work_schedule.SubCommandInWorkSchedule
import com.example.test.dxworkspace.databinding.ItemShiftInDayBinding

class ShiftInDayAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var i : Int = 1 // stt ca
    var code : String =""
    var data = mutableListOf<SubCommandInWorkSchedule?>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onChooseShift : ((shift:Int , day : Int,check : Boolean) -> Unit)? = null
    override fun getItemViewType(position: Int): Int {
        return if(position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShiftViewHolder(ItemShiftInDayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(getItemViewType(position) == 0){
            (holder as ShiftViewHolder).binding.apply {
                ivShift.isVisible = false
                tvShift.isVisible = true
                tvShift.text = "Ca $i"
            }
        } else {
            var item = data[position-1]
            (holder as ShiftViewHolder).binding.apply {
                ivShift.isVisible = true
                tvShift.isVisible = false
                if(item == null) {
                    ivShift.setImageResource(R.drawable.ic_square_work_free)
                } else {
                    if(item!!._id.isEmpty()) {
                        if(!item.isSave) ivShift.setImageResource(R.drawable.ic_square_work_selected)
                        else ivShift.setImageResource(R.drawable.ic_square_work_saved)
                    }
                    else {
                        ivShift.setImageResource(when(item?.status){
                            1 -> R.drawable.ic_square_work_wait
                            2 -> R.drawable.ic_square_work_approved
                            3 -> R.drawable.ic_square_work_inprogress
                            4 -> R.drawable.ic_square_work_finish
                            5 -> R.drawable.ic_square_work_create
                            6 -> R.drawable.ic_square_work_create
                            else -> R.drawable.ic_square_work_create
                        }
                        )
                    }

                }
                root.setOnClickListener {
                    if(item != null && item?._id?.isNotEmpty() || item?.isSave == true) {
                        return@setOnClickListener
                    } else {
                        if(item == null ) {
                            onChooseShift?.invoke(i-1,position,true)
//                            data[position-1] = SubCommandInWorkSchedule(code = code)
                        } else {
                            onChooseShift?.invoke(i-1,position,false)
//                            data[position-1] = null
                        }
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(data.isEmpty()) 0 else data.size + 1
    }

}

class ShiftViewHolder(val binding : ItemShiftInDayBinding) : RecyclerView.ViewHolder(binding.root){

}