package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingPlanModel
import com.example.test.dxworkspace.databinding.ItemDetailDashboardManufacturingPlanBinding
import com.example.test.dxworkspace.databinding.ItemHeaderDashboardManufacturingBinding
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromString

class ManufacturingCommandDashboardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<Pair<List<DashboardManufacturingCommandModel>?,String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClick2:((DashboardManufacturingCommandModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderCommand(
            ItemHeaderDashboardManufacturingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as ViewHolderCommand).bind(item,onClick2)
        (holder as ViewHolderCommand).binding.root.setOnClickListener {
            item.first!![0].isSelected = !item.first!![0].isSelected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolderCommand(val binding: ItemHeaderDashboardManufacturingBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: Pair<List<DashboardManufacturingCommandModel>?,String>,click:((DashboardManufacturingCommandModel) -> Unit)?) {
        val adapter = ManufacturingCommandSubAdapter()
        adapter.onClick = {
            click?.invoke(it)
        }
        binding.apply {
            tvCode.text = item.second
            tvQuantity.text = item.first?.size.toString()
            rcvDetail.adapter = adapter
            adapter.items = item.first!!.toMutableList()
            rcvDetail.isVisible = item.first!![0].isSelected
            ivExpand.setImageResource(if(item.first!![0].isSelected) R.drawable.ic_expand_up else R.drawable.ic_expand_down)
        }
    }
}

class ManufacturingCommandSubAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<DashboardManufacturingCommandModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClick:((DashboardManufacturingCommandModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubViewHolderCommand(
            ItemDetailDashboardManufacturingPlanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as SubViewHolderCommand).bind(item)
        holder.binding.lnRoot.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class SubViewHolderCommand(val binding: ItemDetailDashboardManufacturingPlanBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: DashboardManufacturingCommandModel) {
        binding.apply {
            tvCode.text = item.code
            tvCreator.text = item.manufacturingMill?.name
            tvTime.text = getTimeDDMMYYYYHHMMFromString(item.startDate ?: "")
        }
    }
}