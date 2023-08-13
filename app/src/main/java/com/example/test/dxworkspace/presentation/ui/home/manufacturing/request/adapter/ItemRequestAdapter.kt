package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.databinding.ItemRecycleCreateRequestBinding

class ItemRequestAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<GoodDetailModel>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var isEdit = true
    var onEdit :((pos:Int) -> Unit)? = null
    var onDelete :((pos:Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemRecycleCreateRequestBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val t = items[position]
        (holder as ViewHolder).binding.apply {
            edtVariantName.setText(t.name)
            edtCode.setText(t.code)
            edtPriceInit.setText(t.baseUnit)
            edtLineCost.setText(t.quantity.toString())
            tvDelete.text = "Xóa hàng hóa"
            tvEdit.setOnClickListener { onEdit?.invoke(position) }
            tvDelete.setOnClickListener { onDelete?.invoke(position) }
            tvDelete.visibility = if(isEdit) View.VISIBLE else View.INVISIBLE
            tvEdit.visibility = if(isEdit) View.VISIBLE else View.INVISIBLE
            ivDeleteVariant.visibility = if(isEdit) View.VISIBLE else View.INVISIBLE
            frameTop.visibility = if(isEdit) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = items.size
}
class ViewHolder(val binding : ItemRecycleCreateRequestBinding) : RecyclerView.ViewHolder(binding.root){}