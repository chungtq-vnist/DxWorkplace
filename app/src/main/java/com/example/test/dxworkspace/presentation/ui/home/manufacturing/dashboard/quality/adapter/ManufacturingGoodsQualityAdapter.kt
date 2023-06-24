package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.quality.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.QualityGoodsCompare
import com.example.test.dxworkspace.databinding.ItemDetailGoodsQualityManufacturingBinding
import com.example.test.dxworkspace.presentation.utils.common.hide
import com.example.test.dxworkspace.presentation.utils.common.show


class ManufacturingGoodsQualityAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<QualityGoodsCompare>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var hasCompare : Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SubViewHolderGood(
            ItemDetailGoodsQualityManufacturingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as SubViewHolderGood).bind(item,hasCompare)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class SubViewHolderGood(val binding: ItemDetailGoodsQualityManufacturingBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: QualityGoodsCompare, hasCompare: Boolean) {
        binding.apply {
            tvName.text = item.goods?.name
            tvQuantity.text = ((item.numberOfProducts ?: 0) + (item.numberOfWaste ?: 0)).toString()
            tvQuality.text = item.qualityPercent?.toString() + "%"
            if (!hasCompare) {
                lnCompare.isVisible = false
            } else {
                lnCompare.isVisible = true
                val p1 = item.qualityPercent ?: 0.0
                val p2 = item.qualityPercentConpare
                if (p2 == null) {
                    ivUp1.setImageResource(R.drawable.ic_up)
                    ivUp2.setImageResource(R.drawable.ic_up)
                    ivUp2.show()
                    ivUp1.show()
                    tvCollected.text = ""
                } else {
                    if (p1 > p2) {
                        ivUp1.setImageResource(R.drawable.ic_up)
                        ivUp2.hide()
                        ivUp1.show()
                        tvCollected.text = (p1 - p2).toString() + "%"
                    } else if (p1 == p2) {
                        ivUp2.hide()
                        ivUp1.hide()
                        tvCollected.text = "--"
                    } else {
                        ivUp1.setImageResource(R.drawable.ic_down)
                        ivUp2.hide()
                        ivUp1.show()
                        tvCollected.text = (p2 - p1).toString() + "%"
                    }
                    tvCollected.setTextColor(
                        if (p1 > p2) tvCollected.resources.getColor(R.color.clr_asc) else tvCollected.resources.getColor(
                            R.color.clr_desc
                        )
                    )
                }

            }
        }
    }
}