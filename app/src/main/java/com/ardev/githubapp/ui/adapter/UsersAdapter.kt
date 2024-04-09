package com.ardev.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.databinding.UserCardBinding
import com.bumptech.glide.Glide

class UsersAdapter : ListAdapter<ItemsItem, UsersAdapter.MyViewHolder>(DIFF_CALLBACK) {

    interface OnUserItemClickListener {
        fun onUserItemClicked(user: ItemsItem)
    }

    private var onUserItemClickListener: OnUserItemClickListener? = null

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setOnUserItemClickListener(listener: OnUserItemClickListener) {
        onUserItemClickListener = listener
    }

    class MyViewHolder(private val userCardBinding: UserCardBinding) :
        RecyclerView.ViewHolder(userCardBinding.root) {
        fun bind(user: ItemsItem) {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(userCardBinding.ivUser)
            userCardBinding.tvUserName.text = user.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onUserItemClickListener?.onUserItemClicked(user)
        }


    }

}