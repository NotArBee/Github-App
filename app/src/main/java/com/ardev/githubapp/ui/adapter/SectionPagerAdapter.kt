package com.ardev.githubapp.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ardev.githubapp.ui.fragments.FollowFragment

class SectionPagerAdapter(activity : AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        val args = Bundle().apply {
            putInt(FollowFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        fragment.arguments = args
        return fragment
    }
}