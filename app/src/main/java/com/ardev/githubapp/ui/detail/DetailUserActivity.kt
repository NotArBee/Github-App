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

        // Inisialisasi SectionPagerAdapter dan ViewPager
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager

        // Mengambil username dari intent atau string ekstra
        val username = intent.getStringExtra(MainActivity.EXTRA_DATA) ?: ""

        // Mengatur username ke SectionPagerAdapter
        sectionPagerAdapter.username = username

        // Mengatur adapter ke ViewPager
        viewPager.adapter = sectionPagerAdapter

        // Mengatur TabLayout
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        // Mengamati perubahan pada data pengguna
        userDetailViewModel.detailUser.observe(this) { userDetail ->
            setUserData(userDetail)
        }

        // Mengamati perubahan pada status loading
        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Meminta ViewModel untuk menampilkan detail pengguna
        val userLogin = intent.getStringExtra(MainActivity.EXTRA_DATA)
        userDetailViewModel.showUserDetail(userLogin ?: "")
    }

    // Metode untuk mengatur data pengguna pada tampilan
    private fun setUserData(user: DetailUserResponse) {
        binding.apply {
            Glide.with(this@DetailUserActivity)
                .load(user.avatarUrl)
                .into(ivUserProfile)
            tvDetailUserName.text = user.login
            tvDetailRealName.text = user.name
            tvFollowers.text = "${user.followers.toString()} Followers"
            tvFollowing.text = "${user.following.toString()} Following"
        }
    }

    // Metode untuk menampilkan atau menyembunyikan status loading
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Daftar judul tab
    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val EXTRA_FRAGMENT = "extra_fragment"
    }
}