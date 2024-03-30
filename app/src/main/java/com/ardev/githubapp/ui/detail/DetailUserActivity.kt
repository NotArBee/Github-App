package com.ardev.githubapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ardev.githubapp.data.response.DetailUserResponse
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private val userDetailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        val user = intent.getParcelableExtra<ItemsItem>(EXTRA_USER)
        if (user != null) {
            userDetailViewModel.showUserDetail(user.login)
        } else {
            Log.e("DetailUserActivity", "Error di intent parcelable")
        }

        userDetailViewModel.detailUser.observe(this) { userDetail ->
            setUserData(userDetail)
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }


    }

    private fun setUserData(username: DetailUserResponse) {
        binding.apply {
            Glide.with(this@DetailUserActivity)
                .load(username.avatarUrl)
                .into(ivUserProfile)
            tvDetailUserName.text = username.login
            tvDetailRealName.text = username.name
            tvFollowers.text = "${username.followers.toString()} Followers"
            tvFollowing.text = "${username.following.toString()} Following"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}