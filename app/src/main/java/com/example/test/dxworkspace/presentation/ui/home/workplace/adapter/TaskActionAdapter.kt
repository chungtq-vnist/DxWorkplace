package com.example.test.dxworkspace.presentation.ui.home.workplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.dxworkspace.BuildConfig
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.di.NetworkModule
import com.example.test.dxworkspace.data.entity.task.Comment
import com.example.test.dxworkspace.data.entity.task.TaskAction
import com.example.test.dxworkspace.data.entity.task.TaskModel
import com.example.test.dxworkspace.databinding.ItemActionTaskBinding
import com.example.test.dxworkspace.databinding.ItemCommentActionTaskBinding
import com.example.test.dxworkspace.databinding.ItemTaskBinding

class TaskActionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: MutableList<TaskAction> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskActionViewHolder(
            ItemActionTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val taskAction = items[position]
        (holder as TaskActionViewHolder).apply {
            bind(taskAction)
        }
    }

    override fun getItemCount(): Int = items.size
}

class TaskActionViewHolder(val binding: ItemActionTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TaskAction) {
        binding.apply {
            Glide.with(ivUser)
                .load((if(BuildConfig.DEBUG) NetworkModule.BASE_URL_DEV else NetworkModule.BASE_URL)
                    .plus(item.creator?.avatar))
                .placeholder(ivUser.resources.getDrawable(R.drawable.user))
                .error(ivUser.resources.getDrawable(R.drawable.user))
                .into(ivUser)
            tvAction.text = item.description
            tvUserName.text = item.creator?.name
            val commentAdapter = TaskCommentAdapter()
            rcvReply.adapter = commentAdapter
            commentAdapter.items = item.comments?.toMutableList() ?: mutableListOf<Comment>()
        }
    }
}

class TaskCommentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var items: MutableList<Comment> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskCommentViewHolder(ItemCommentActionTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val i = items[position]
        (holder as TaskCommentViewHolder).apply {
            bind(i)
        }
    }

    override fun getItemCount(): Int = items.size
}

class TaskCommentViewHolder(val binding : ItemCommentActionTaskBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item : Comment){
        binding.apply {
            Glide.with(ivUser)
                .load((if(BuildConfig.DEBUG) NetworkModule.BASE_URL_DEV else NetworkModule.BASE_URL)
                    .plus(item.creator?.avatar))
                .placeholder(ivUser.resources.getDrawable(R.drawable.user))
                .error(ivUser.resources.getDrawable(R.drawable.user))
                .into(ivUser)
            tvAction.text = item.description
            tvUserName.text = item.creator?.name
        }


    }
}