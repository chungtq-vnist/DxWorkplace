package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingPlanModel
import com.example.test.dxworkspace.databinding.ItemDetailDashboardManufacturingPlanBinding
import com.example.test.dxworkspace.databinding.ItemHeaderDashboardManufacturingBinding
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromString

class ManufacturingPlanDashboardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<Pair<List<DashboardManufacturingPlanModel>?,String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClick2:((DashboardManufacturingPlanModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderPlan(
            ItemHeaderDashboardManufacturingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as ViewHolderPlan).bind(item,onClick2)
        (holder as ViewHolderPlan).binding.root.setOnClickListener {
            item.first!![0].isSelected = !item.first!![0].isSelected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolderPlan(val binding: ItemHeaderDashboardManufacturingBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: Pair<List<DashboardManufacturingPlanModel>?,String>,click :((DashboardManufacturingPlanModel)-> Unit)?) {
        val adapter = ManufacturingPlanSubAdapter()
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

class ManufacturingPlanSubAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<DashboardManufacturingPlanModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClick:((DashboardManufacturingPlanModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubViewHolderPlan(
            ItemDetailDashboardManufacturingPlanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as SubViewHolderPlan).bind(item)
        holder.binding.lnRoot.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class SubViewHolderPlan(val binding: ItemDetailDashboardManufacturingPlanBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: DashboardManufacturingPlanModel) {
        binding.apply {
            tvCode.text = item.code
            tvCreator.text = item.creator.name
            tvTime.text = getTimeDDMMYYYYHHMMFromString(item.startDate ?: "")
        }
    }
}