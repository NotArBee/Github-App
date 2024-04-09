package com.ardev.githubapp.ui.activity.main

import SettingViewModel
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.githubapp.R
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.databinding.ActivityMainBinding
import com.ardev.githubapp.ui.activity.Setting.SettingActivity
import com.ardev.githubapp.ui.activity.Setting.SettingPreferences
import com.ardev.githubapp.ui.activity.Setting.SettingViewModelFactory
import com.ardev.githubapp.ui.activity.Setting.dataStore
import com.ardev.githubapp.ui.adapter.UsersAdapter
import com.ardev.githubapp.ui.activity.detail.DetailUserActivity
import com.ardev.githubapp.ui.activity.favoriteuser.FavoriteUserActivity

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA = "extra_data"

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

        val pref = SettingPreferences.getInstance(dataStore)
        val themeSettingView =
            ViewModelProvider(this, SettingViewModelFactory(pref)).get(SettingViewModel::class.java)

        themeSettingView.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_layout -> binding.rvUser.layoutManager = LinearLayoutManager(this)
            R.id.grid_layout -> binding.rvUser.layoutManager = GridLayoutManager(this, 2)
            R.id.settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }

            R.id.favorite_activity -> {
                val intent = Intent(this, FavoriteUserActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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
                intentDetail.putExtra(EXTRA_DATA, user)
                startActivity(intentDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}