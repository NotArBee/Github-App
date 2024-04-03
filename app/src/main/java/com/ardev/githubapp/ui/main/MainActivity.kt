package com.ardev.githubapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.databinding.ActivityMainBinding
import com.ardev.githubapp.ui.adapter.UsersAdapter
import com.ardev.githubapp.ui.detail.DetailUserActivity

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val USERNAME = "Arya"
    }

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)


        mainViewModel.itemsItem.observe(this) { itemsItem ->
            setGithubUsersData(itemsItem)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val search = searchView.text.toString()
                    mainViewModel.searchUsers(search)
                    searchView.hide()
                    false
                }
        }
    }

    private fun setGithubUsersData(users: List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (i in users) {
            listUser.clear()
            listUser.addAll(users)
        }

        val adapter = UsersAdapter()
        adapter.submitList(users)
        binding.rvUser.adapter = adapter


        adapter.setOnUserItemClickListener(object : UsersAdapter.OnUserItemClickListener {
            override fun onUserItemClicked(user: ItemsItem) {
                val intentDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentDetail.putExtra(DetailUserActivity.EXTRA_USER, user)
                startActivity(intentDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}