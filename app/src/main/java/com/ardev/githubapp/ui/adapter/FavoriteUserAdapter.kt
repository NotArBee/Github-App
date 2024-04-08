package com.ardev.githubapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ardev.githubapp.data.response.DetailUserResponse
import com.ardev.githubapp.database.FavoriteUser
import com.ardev.githubapp.databinding.UserCardBinding
import com.ardev.githubapp.ui.activity.detail.DetailUserActivity
import com.ardev.githubapp.ui.activity.main.MainActivity
import com.ardev.githubapp.ui.helper.FavoriteUserDiffCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class FavoriteUserAdapter :
    ListAdapter<FavoriteUser, FavoriteUserAdapter.FavoriteUserViewHolder>(DIFF_CALLBACK) {

    private val listFavUser = ArrayList<FavoriteUser>()

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteUser> =
            object : DiffUtil.ItemCallback<FavoriteUser>() {
                override fun areItemsTheSame(oldUser: FavoriteUser, newUser: FavoriteUser): Boolean {
                    return oldUser.login == newUser.login
                }

                override fun areContentsTheSame(oldUser: FavoriteUser, newUser: FavoriteUser): Boolean {
                    return oldUser == newUser
                }
            }
    }

    class FavoriteUserViewHolder(val binding: UserCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind (favoriteUser: FavoriteUser) {
                with(binding) {
                    tvUserName.text = favoriteUser.login
                    Glide.with(itemView.context)
                        .load(favoriteUser.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivUser)
                    itemView.setOnClickListener {
                        val intent = Intent(it.context, DetailUserActivity::class.java)
                        intent.putExtra(MainActivity.EXTRA_DATA, favoriteUser.login)
                        itemView.context.startActivity(intent)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavUser.size

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        val favorites = listFavUser[position]
        holder.bind(favorites)
    }

    fun setListFavoriteUer(listUser: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(this.listFavUser, listUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavUser.clear()
        this.listFavUser.addAll(listUser)
        diffResult.dispatchUpdatesTo(this)
    }
}