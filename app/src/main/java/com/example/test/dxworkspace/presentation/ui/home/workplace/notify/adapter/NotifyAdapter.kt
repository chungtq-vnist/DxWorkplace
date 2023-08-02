package com.example.test.dxworkspace.presentation.ui.home.workplace.notify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.notify.NotificationModel
import com.example.test.dxworkspace.databinding.ItemNotifyBinding
import com.example.test.dxworkspace.presentation.utils.common.toHtml
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventGoToNotification
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromStringOneLine

class NotifyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<NotificationModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var markReaded : ((String) ->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotifyViewHolder(ItemNotifyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as NotifyViewHolder).apply {
            binding.apply {
                tvHeader.text = item.title
                val htmlDescription = (item.content ?: "").toHtml()
                val descriptionWithOutExtraSpace = (htmlDescription.toString()).trim { it <= ' ' }
                tvBody.setText(htmlDescription?.subSequence(0, descriptionWithOutExtraSpace.length))
                tvTime.text = getTimeDDMMYYYYHHMMFromStringOneLine(item.createdAt)
                when (item.associatedDataObject?.dataType) {
                    1 -> {
                        ivImage.setImageResource(R.drawable.ic_notify_cart)
                    }
                    2 -> {
                        ivImage.setImageResource(R.drawable.ic_notify_asset)
                    }
                    3 -> {
                        ivImage.setImageResource(R.drawable.ic_notify_kpi)
                    }
                    5 -> {
                        ivImage.setImageResource(R.drawable.ic_notify_product)
                    }
                    6 -> {
                        ivImage.setImageResource(R.drawable.ic_notify_procedure)
                    }
                    else -> {
                        ivImage.setImageResource(R.drawable.ic_notify_cart)
                    }
                }
                if (item.readed) {
                    holder.binding.rootView.setBackgroundResource(R.color.white)
                } else {
                    holder.binding.rootView.setBackgroundResource(R.color.sp_color_notification)
                }
                rootView.setOnClickListener {
                    if(!item.readed) {
                        markReaded?.invoke(item.id)
                        item.readed = true
                        notifyItemChanged(position)
                    }
                    when (item.associatedDataObject?.dataType) {
                        1 -> {
                            ivImage.setImageResource(R.drawable.ic_notify_cart)
                            EventBus.getDefault().post(EventGoToNotification(EventGoToNotification.DETAIL_TASK))
                        }
                        2 ,3 ,6-> {
                            // show dialog thong tin thong bao
                            EventBus.getDefault().post(EventGoToNotification(EventGoToNotification.DIALOG_DETAIL,
                                listOf(item.title,item.content)))

                        }
                        5 -> {
                            ivImage.setImageResource(R.drawable.ic_notify_product)
                            EventBus.getDefault().post(EventGoToNotification(EventGoToNotification.DETAIL_REQUEST))
                        }
                        else -> {
                            ivImage.setImageResource(R.drawable.ic_notify_cart)
                            EventBus.getDefault().post(EventGoToNotification(EventGoToNotification.DIALOG_DETAIL,
                                listOf(item.title,item.content)))
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun appendOrder(notifications: List<NotificationModel>) {
        if (notifications.isEmpty()) {
            notifyItemChanged(itemCount - 1)
        } else {
            items.addAll(notifications)
            notifyItemInserted(itemCount - notifications.size)
        }
    }
}

class NotifyViewHolder(val binding : ItemNotifyBinding) : RecyclerView.ViewHolder(binding.root){

}