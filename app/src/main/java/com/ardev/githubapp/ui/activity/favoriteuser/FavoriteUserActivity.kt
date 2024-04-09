package com.ardev.githubapp.ui.activity.favoriteuser

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.githubapp.database.FavoriteUser
import com.ardev.githubapp.databinding.ActivityFavoriteUsersBinding
import com.ardev.githubapp.ui.adapter.FavoriteUserAdapter
import com.ardev.githubapp.ui.helper.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUsersBinding
    private lateinit var viewModel: FavoriteUserViewModel
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = obtainViewModel(this@FavoriteUserActivity)

        adapter = FavoriteUserAdapter()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.getAllFavoriteUsers().observe(this) { users ->
            val favoriteUsers = users.map { user ->
                FavoriteUser(login = user.login, avatarUrl = user.avatarUrl)
            }
            adapter.submitList(favoriteUsers)
            adapter.setListFavoriteUser(users)
        }

        binding.rvFavUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        binding.rvFavUser.addItemDecoration(itemDecoration)
        binding.rvFavUser.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}