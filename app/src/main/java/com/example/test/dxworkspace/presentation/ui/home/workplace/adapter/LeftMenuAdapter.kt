package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.ItemDashViewBinding
import com.example.test.dxworkspace.databinding.ItemMenuChildBinding
import com.example.test.dxworkspace.databinding.ItemMenuHeaderBinding
import com.example.test.dxworkspace.databinding.ItemMenuNormalBinding
import com.example.test.dxworkspace.presentation.model.menu.MenuModel

class LeftMenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((MenuModel) -> Unit)? = null

    companion object {
        const val MENU_ITEM_HEADER = 1
        const val MENU_ITEM_NORMAL = 2
        const val MENU_ITEM_CHILD = 3
        const val MENU_ITEM_LINE = 4
    }

    var items = mutableListOf<MenuModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /** item view type :
     * 1 : menu header
     * 2 : menu normal
     * 3 : menu child
     * 4 : line
     */

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return item.level
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            MENU_ITEM_HEADER -> {
                MenuItemHeaderViewHolder(
                    ItemMenuHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MENU_ITEM_NORMAL -> {
                MenuItemViewHolder(
                    ItemMenuNormalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MENU_ITEM_CHILD -> {
                MenuItemChildViewHolder(
                    ItemMenuChildBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MENU_ITEM_LINE -> {
                MenuItemLineViewHolder(
                    ItemDashViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                MenuItemLineViewHolder(
                    ItemDashViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            MENU_ITEM_HEADER -> {
                (holder as MenuItemHeaderViewHolder).apply {
                    bind(item)
                }
            }
            MENU_ITEM_NORMAL -> {
                (holder as MenuItemViewHolder).apply {
                    binding.rootView.setOnClickListener { onItemClick?.invoke(item) }
                    bind(item)
                }

            }
            MENU_ITEM_CHILD -> {
                (holder as MenuItemChildViewHolder).apply {
                    binding.rootView.setOnClickListener { onItemClick?.invoke(item) }
                    bind(item)
                }

            }
            else -> {

            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun onExpandMenu(menu : MenuModel){
        items.forEach {
            if(it.category == menu.category){
                it.isVisible = menu.isExpand
            }
        }
        notifyDataSetChanged()
    }

}

class MenuItemHeaderViewHolder(
    val binding: ItemMenuHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(menu: MenuModel) {
        binding.tvMenu.setText(menu.desc)
    }
}

class MenuItemViewHolder(
    val binding: ItemMenuNormalBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: MenuModel) {
        binding.apply {
            ivMenu.setImageResource(model.iconStart)
            tvMenu.setText(model.desc)
            ivMenuEnd.isVisible = model.iconEnd != 0
            if (model.iconEnd != 0) {
                model.iconEnd =
                    if (model.isExpand) R.drawable.ic_up_menu else R.drawable.ic_down_menu
                ivMenuEnd.setBackgroundResource(model.iconEnd)

            }

        }
    }
}

class MenuItemChildViewHolder(
    val binding: ItemMenuChildBinding,
) : RecyclerView.ViewHolder(binding.root) {
    val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    fun bind(model: MenuModel) {
        binding.apply {
            if (model.iconStart != 0) ivMenu.setImageResource(model.iconStart)
            tvMenu.setText(model.desc)
            if (model.isVisible) layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            else layoutParams.height = 0
            root.layoutParams = layoutParams
        }
    }
}

class MenuItemLineViewHolder(
    val binding: ItemDashViewBinding,
) : RecyclerView.ViewHolder(binding.root)