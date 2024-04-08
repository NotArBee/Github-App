package com.ardev.githubapp.ui.helper

import androidx.recyclerview.widget.DiffUtil
import com.ardev.githubapp.database.FavoriteUser

class FavoriteUserDiffCallback(private val oldFavUserList: List<FavoriteUser>, private val newFavUserList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavUserList.size

    override fun getNewListSize(): Int = oldFavUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavUserList[oldItemPosition].login == newFavUserList[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavUser = oldFavUserList[oldItemPosition]
        val newFavUser = newFavUserList[newItemPosition]
        return oldFavUser.login == newFavUser.login && oldFavUser.avatarUrl == oldFavUser.avatarUrl
    }

}