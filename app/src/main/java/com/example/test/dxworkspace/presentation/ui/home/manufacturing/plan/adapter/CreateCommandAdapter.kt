package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.databinding.ItemRecycleCreateCommandBinding
import com.example.test.dxworkspace.databinding.ItemRecycleProductCreateCommandBinding
import com.example.test.dxworkspace.domain.model.manufacturing_plan.SubRequestCommand

class CreateCommandAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listgood = listOf<GoodDetailModel>()
    var listInventory = listOf<InventoryGoodWrap>()
    var listInfoGood = listOf<DataInfoProduct>()
    var data = listOf<SubRequestCommand>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onEdit : ((pos:Int , i : SubRequestCommand) -> Unit)? = null
    var onRemove : ((pos:Int , i : SubRequestCommand) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CreateCommandViewHolder(
            ItemRecycleCreateCommandBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        (holder as CreateCommandViewHolder).apply {
            binding.apply {
                val good = listgood.find { it._id == item.goodID }
                val context = edtVariantName.context
                edtVariantName.setText(good?.name)
                edtCode.setText(item.code)
                edtPriceInit.setText(item.quantity.toString())
                edtLineCost.setText(context.getString(R.string.number_user,item.approvers.size.toString()))
                edtObserver.setText(context.getString(R.string.number_user,item.accountables.size.toString()))
                edtTester.setText(context.getString(R.string.number_user,item.qualityControlStaffs.size.toString()))

                tvEdit.setOnClickListener {
                    onEdit?.invoke(position,item)
                }
                tvDelete.setOnClickListener {
                    onRemove?.invoke(position,item)
                }
                ivDeleteVariant.setOnClickListener {
                    onRemove?.invoke(position,item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class CreateCommandViewHolder(val binding : ItemRecycleCreateCommandBinding) : RecyclerView.ViewHolder(binding.root){

}