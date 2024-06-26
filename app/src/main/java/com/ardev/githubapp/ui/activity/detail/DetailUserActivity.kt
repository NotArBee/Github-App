package com.ardev.githubapp.ui.activity.detail

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ardev.githubapp.R
import com.ardev.githubapp.data.response.DetailUserResponse
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.database.FavoriteUser
import com.ardev.githubapp.databinding.ActivityDetailUserBinding
import com.ardev.githubapp.ui.adapter.SectionPagerAdapter
import com.ardev.githubapp.ui.activity.main.MainActivity
import com.ardev.githubapp.ui.helper.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userDetailViewModel: DetailViewModel
    private var isFavorite: Boolean = false
    private var favoriteUser: FavoriteUser? = null

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
        val user: Parcelable? = intent.getParcelableExtra(MainActivity.EXTRA_DATA)
        if (user is ItemsItem) {
            handleItemsItem(user)
        } else if (user is FavoriteUser) {
            handleFavoriteUser(user)
        } else {
            println("Data Kosong")
        }

    }

    private fun handleFavoriteUser(user: FavoriteUser) {
        userDetailViewModel = obtainViewModel(this)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager

        sectionPagerAdapter.username = user.login

        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        userDetailViewModel.detailUser.observe(this) { userDetail ->
            setUserData(userDetail)
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        userDetailViewModel.showUserDetail(user.login)

        favoriteUser = FavoriteUser(user.login, user.avatarUrl)

        userDetailViewModel.getFavoriteUser().observe(this) {
            isFavorite = it.contains(favoriteUser)
            if (isFavorite) {
                @Suppress("DEPRECATION")
                binding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.favorite_icon_blue))
            } else {
                @Suppress("DEPRECATION")
                binding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.favorite_icon_notfill))
            }

        }

        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                userDetailViewModel.delete(favoriteUser!!)
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.deleteDatabase)),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    resources.getString(R.string.undo)
                ) {
                    userDetailViewModel.insert(favoriteUser!!)
                }.show()
            } else {
                userDetailViewModel.insert(favoriteUser!!)
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.insertDatabase)),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    resources.getString(R.string.undo)
                ) {
                    userDetailViewModel.delete(favoriteUser!!)
                }.show()
            }
        }
    }

    private fun handleItemsItem(user: ItemsItem) {
        userDetailViewModel = obtainViewModel(this)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager

        sectionPagerAdapter.username = user.login.toString()

        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        userDetailViewModel.detailUser.observe(this) { userDetail ->
            setUserData(userDetail)
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        userDetailViewModel.showUserDetail(user.login.toString())

        favoriteUser = FavoriteUser(user.login.toString(), user.avatarUrl)

        userDetailViewModel.getFavoriteUser().observe(this) {
            isFavorite = it.contains(favoriteUser)
            if (isFavorite) {
                @Suppress("DEPRECATION")
                binding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.favorite_icon_blue))
            } else {
                @Suppress("DEPRECATION")
                binding.fabFavorite.setImageDrawable(resources.getDrawable(R.drawable.favorite_icon_notfill))
            }

        }

        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                userDetailViewModel.delete(favoriteUser!!)
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.deleteDatabase)),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    resources.getString(R.string.undo)
                ) {
                    userDetailViewModel.insert(favoriteUser!!)
                }.show()
            } else {
                userDetailViewModel.insert(favoriteUser!!)
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.insertDatabase)),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    resources.getString(R.string.undo)
                ) {
                    userDetailViewModel.delete(favoriteUser!!)
                }.show()
            }
        }
    }

    private fun setUserData(user: DetailUserResponse) {
        val userNickname = user.name
        supportActionBar?.title = userNickname
        binding.apply {
            Glide.with(this@DetailUserActivity).load(user.avatarUrl).into(ivUserProfile)
            tvDetailUserName.text = user.login
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }
}