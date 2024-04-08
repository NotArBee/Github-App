package com.ardev.githubapp.ui.activity.favoriteuser

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.githubapp.R
import com.ardev.githubapp.databinding.ActivityFavoriteUsersBinding
import com.ardev.githubapp.ui.adapter.FavoriteUserAdapter
import com.ardev.githubapp.ui.helper.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUsersBinding
//    private val viewModel by viewModels<FavoriteUserViewModel>()
    private lateinit var viewModel: FavoriteUserViewModel
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = FavoriteUserAdapter()

        viewModel = obtainViewModel(this@FavoriteUserActivity)
        viewModel.getAllFavoriteUsers().observe(this) { githubUserList ->
            if (githubUserList != null) {
                adapter.setListFavoriteUer(githubUserList)
            }
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
}