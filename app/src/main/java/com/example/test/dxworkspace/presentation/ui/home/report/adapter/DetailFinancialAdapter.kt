package com.example.test.dxworkspace.presentation.ui.home.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.ItemDetailDashboardBinding
import com.example.test.dxworkspace.databinding.ItemDetailDashboardHeaderBinding
import com.example.test.dxworkspace.presentation.model.menu.CompareModel
import com.example.test.dxworkspace.presentation.utils.common.formatMoney
import com.example.test.dxworkspace.presentation.utils.common.hide
import com.example.test.dxworkspace.presentation.utils.common.show

class DetailFinancialAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_NORMAL = 1

    var items = mutableListOf<CompareModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) return HeaderViewHolder(
            ItemDetailDashboardHeaderBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )
        else return DetailNormalViewHolder(
            ItemDetailDashboardBinding.inflate(
                LayoutInflater.from(
                    viewGroup.context
                ), viewGroup, false
            )
        )
    }

    override fun getItemCount() = if (items.isEmpty()) 0 else (items.size + 1)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        when {
            getItemViewType(pos) == TYPE_HEADER -> {

            }
            getItemViewType(pos) == TYPE_NORMAL -> {
                val item = items[pos - 1]
                (holder as DetailNormalViewHolder).binding.apply {
                    tvRevenueTitle.text = item.title
                    ivTypeRevenue.setImageResource(item.iconId)
//                    ivUp1.hide()
//                    ivUp2.hide()
//                    tvCollected.hide()
                    tvMoneyRevenue.text =
                        if (item.type == "money") formatMoney(item.now) else item.now.toInt()
                            .toString()
                    val percent =
                        if (item.compare == 0.0 && item.now != 0.0) 1000.0 else if (item.compare == 0.0 && item.now == 0.0) 100.0
                        else ((item.now / item.compare) * 10000).toLong() / 100.0
                    if (percent == 1000.0) {
                        ivUp1.show()
                        ivUp2.show()
                        ivUp1.setImageResource(R.drawable.ic_up)
                        ivUp2.setImageResource(R.drawable.ic_up)
                        tvCollected.hide()
                    } else if (percent > 100.0) {
                        ivUp1.show()
                        ivUp2.hide()
                        tvCollected.show()
                        ivUp1.setImageResource(R.drawable.ic_up)
                        tvCollected.text = "${percent - 100} %"
                    } else if (percent < 100.0) {
                        ivUp1.show()
                        ivUp2.hide()
                        tvCollected.show()
                        ivUp1.setImageResource(R.drawable.ic_down)
                        tvCollected.text = "${100.0 - percent} %"
                    } else {
                        ivUp1.hide()
                        ivUp2.hide()
                        tvCollected.show()
                        tvCollected.text = "--"
                    }
                }
//                with(holder.itemView) {
//
//                    var amount = item.amount
//                    if (amount == -0.0) {
//                        amount = abs(amount)
//                    }
//                    if ((item.name == AVERAGE || item.name == AVERAGE_ORDER_VALUE) && item.hiddenValue == true) {
//                        val value = "--"
//                        tvMoneyRevenue.text = value
//                    } else {
//                        if (item.name == ApiConstants.ORDER_COUNT_TOTAL || item.name == ApiConstants.ORDER_CANCEL || item
//                                .name == ApiConstants.ITEM_QUANTITY || item.name == AVERAGE
//                        ) {
//                            tvMoneyRevenue.text = FormatUtil.formatDouble(
//                                amount,
//                                isAcceptMinus = true,
//                                isAcceptZero = true
//                            )
//                            tvMoneyRevenueOther.text = FormatUtil.formatDouble(
//                                item.amount,
//                                isAcceptMinus = true,
//                                isAcceptZero = true
//                            )
//                        } else {
//                            tvMoneyRevenue.text = FormatUtil.formatMoney(
//                                amount,
//                                isAcceptMinus = true,
//                                isAcceptZero = true
//                            )
//                            tvMoneyRevenueOther.text = FormatUtil.formatMoney(
//                                amount, isAcceptMinus = true,
//                                isAcceptZero = true
//                            )
//                        }
//                    }
//
//                    if (item.name == AVERAGE_ORDER_VALUE && tvMoneyRevenue.text.length > 12) {
//                        tvMoneyRevenueOther.show()
//                        tvMoneyRevenue.hide()
//                    }
//
//                    val collectionStatus = getCollectedStatus(item.amount, item.amountCompare)
//                    tvCollected.setTextColorz(if (collectionStatus.status) R.color.clr_asc else R.color.clr_desc)
//
//                    if (collectionStatus.showValue) {
//                        ivUp1.show()
//                        //up
//                        if (collectionStatus.status) {
//                            if (collectionStatus.hasValue) {
//                                ivUp1.setImageResource(R.drawable.ic_up)
//                                ivUp2.hide()
//                            } else {
//                                ivUp1.setImageResource(R.drawable.ic_up)
//                                ivUp2.setImageResource(R.drawable.ic_up)
//                                ivUp2.show()
//                            }
//                        } else {
//                            //down
//                            if (collectionStatus.hasValue) {
//                                ivUp1.setImageResource(R.drawable.ic_down)
//                                ivUp2.hide()
//                            } else {
//                                ivUp1.setImageResource(R.drawable.ic_down)
//                                ivUp2.setImageResource(R.drawable.ic_down)
//                                ivUp2.show()
//                            }
//                        }
//
//                        tvCollected.isVisible = isCompare
//
//                        if (!isCompare || ((item.name == AVERAGE || item.name == AVERAGE_ORDER_VALUE) && item.hiddenValue == true)) {
//                            ivUp1.isVisible = false
//                            ivUp2.isVisible = false
//                            tvCollected.isVisible = false
//                        }
//                    } else {
//                        ivUp1.hide()
//                        ivUp2.hide()
//                        tvCollected.isVisible = isCompare
//                    }
//                    tvCollected.text = collectionStatus.value
//                }
            }
        }
    }
}

class HeaderViewHolder(val binding: ItemDetailDashboardHeaderBinding) : RecyclerView.ViewHolder(
    binding.root
) {

}

class DetailNormalViewHolder(val binding: ItemDetailDashboardBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: CompareModel) {

    }
}