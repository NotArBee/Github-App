package com.ardev.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ardev.githubapp.R
import com.ardev.githubapp.data.response.DetailUserResponse
import com.ardev.githubapp.databinding.FragmentFollowBinding
import com.ardev.githubapp.databinding.UserCardBinding
import com.bumptech.glide.Glide

class ListFollowAdapter(private val listUser: List<DetailUserResponse>) :
    RecyclerView.Adapter<ListFollowAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.binding.tvUserName.text = user.login
        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .into(holder.binding.ivUser)
    }

}