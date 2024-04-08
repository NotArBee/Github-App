package com.ardev.githubapp.ui.activity.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ardev.githubapp.R
import com.ardev.githubapp.data.response.DetailUserResponse
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

    //    private val userDetailViewModel by viewModels<DetailViewModel>(){
//        ViewModelFactory.getInstance(application)
//    }
    private lateinit var userDetailViewModel: DetailViewModel
    private var fabFavorite: Boolean = false
    private var favoriteUser: FavoriteUser? = null
    private var favUser: FavoriteUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userDetailViewModel = obtainViewModel(this)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager

        val username = intent.getStringExtra(MainActivity.EXTRA_DATA) ?: ""

        sectionPagerAdapter.username = username

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

        val userLogin = intent.getStringExtra(MainActivity.EXTRA_DATA)
        userDetailViewModel.showUserDetail(userLogin ?: "")

//
//        favUser = intent.getParcelableExtra(EXTRA_FAVUSER)
////        if (favUser != null) {
////
////        }
//
//        binding.fabFavorite.setOnClickListener{
//            userDetailViewModel.insert(favUser as FavoriteUser)
//        }
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