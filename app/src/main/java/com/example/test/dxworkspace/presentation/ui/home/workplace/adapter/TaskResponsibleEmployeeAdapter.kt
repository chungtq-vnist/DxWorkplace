package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.dxworkspace.BuildConfig
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.di.NetworkModule
import com.example.test.dxworkspace.data.entity.task.TaskAction
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.ItemActionTaskBinding
import com.example.test.dxworkspace.databinding.ItemResponsibleEmployeeTaskBinding

class TaskResponsibleEmployeeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: MutableList<UserProfileResponse> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskResponsibleEmplViewHolder(
            ItemResponsibleEmployeeTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val taskAction = items[position]
        (holder as TaskResponsibleEmplViewHolder).apply {
            bind(taskAction)
        }
    }

    override fun getItemCount(): Int = items.size
}

class TaskResponsibleEmplViewHolder(val binding: ItemResponsibleEmployeeTaskBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: UserProfileResponse) {
        binding.apply {
            Glide.with(ivUser)
                .load(
                    (if (BuildConfig.DEBUG) NetworkModule.BASE_URL_DEV else NetworkModule.BASE_URL)
                        .plus(item.avatar)
                )
                .placeholder(ivUser.resources.getDrawable(R.drawable.user))
                .error(ivUser.resources.getDrawable(R.drawable.user))
                .into(ivUser)
            tvUserName.text = item.name
        }
    }
}