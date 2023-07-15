package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.DashboardManufacturingRequestModel
import com.example.test.dxworkspace.databinding.ItemDetailDashboardManufacturingPlanBinding
import com.example.test.dxworkspace.databinding.ItemHeaderDashboardManufacturingBinding
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromString

class ManufacturingRequestDashboardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<Pair<List<DashboardManufacturingRequestModel>?,String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClick2:((DashboardManufacturingRequestModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderRequest(
            ItemHeaderDashboardManufacturingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as ViewHolderRequest).bind(item,onClick2)
        (holder as ViewHolderRequest).binding.root.setOnClickListener {
            item.first!![0].isSelected = !item.first!![0].isSelected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolderRequest(val binding: ItemHeaderDashboardManufacturingBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: Pair<List<DashboardManufacturingRequestModel>?,String>,click:((DashboardManufacturingRequestModel)-> Unit)?) {
        val adapter = ManufacturingRequestSubAdapter()
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

class ManufacturingRequestSubAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<DashboardManufacturingRequestModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClick:((DashboardManufacturingRequestModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubViewHolderRequest(
            ItemDetailDashboardManufacturingPlanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as SubViewHolderRequest).bind(item)
        holder.binding.lnRoot.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class SubViewHolderRequest(val binding: ItemDetailDashboardManufacturingPlanBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: DashboardManufacturingRequestModel) {
        binding.apply {
            tvCode.text = item.code
            tvCreator.text = item.creator.name
            tvTime.text = getTimeDDMMYYYYHHMMFromString(item.createdAt ?: "")
        }
    }
}