package com.ardev.githubapp.ui.activity.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userDetailViewModel: DetailViewModel
    private var fabFavorite: Boolean = false
    private var favoriteUser: FavoriteUser? = null
    private var detailUser = DetailUserResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val user = intent.getParcelableExtra<ItemsItem>(MainActivity.EXTRA_DATA) as ItemsItem

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

        binding.fabFavorite.setOnClickListener {
            userDetailViewModel.insert(favoriteUser!!)
        }
    }

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
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val EXTRA_FAVUSER = "extra_favuser"
    }
}