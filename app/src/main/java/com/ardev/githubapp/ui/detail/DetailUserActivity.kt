package com.ardev.githubapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.ardev.githubapp.R
import com.ardev.githubapp.data.response.DetailUserResponse
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.databinding.ActivityDetailUserBinding
import com.ardev.githubapp.ui.adapter.SectionPagerAdapter
import com.ardev.githubapp.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

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

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val userIntent = intent.extras
        if (userIntent != null) {
            val userLogin = userIntent.getString(MainActivity.EXTRA_DATA)
            userDetailViewModel.showUserDetail(userLogin!!)
        } else {
            Log.e("DetailUserActivity", "Error in getting parcelable intent")
        }

        userDetailViewModel.detailUser.observe(this) { userDetail ->
            setUserData(userDetail)
            var username = userDetail.login
            // Set data username pada adapter
            sectionPagerAdapter.username = username ?: "Gagal"
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

    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}