package com.ardev.githubapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ardev.githubapp.databinding.FragmentFollowBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.githubapp.R
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.ui.adapter.ListFollowAdapter
import com.ardev.githubapp.ui.detail.DetailUserActivity
import com.ardev.githubapp.ui.detail.DetailViewModel

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel

    @Suppress("UNREACHABLE_CODE")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        @Suppress("DEPRECATION")
        val gitHubUser: ItemsItem =
            requireActivity().intent.getParcelableExtra(DetailUserActivity.EXTRA_USER)!!
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        binding.rvFollow.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)

        val tabTitle = arguments?.getString(DetailUserActivity.TAB_TITLES.toString())
        if (tabTitle == getString(R.string.tab_text_1)) {
            viewModel.getFollowersList(gitHubUser.login)
        } else if (tabTitle == getString(R.string.tab_text_2)) {
            viewModel.getFollowingList(gitHubUser.login)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.listFollow.observe(viewLifecycleOwner) { listFollow ->
            val adapter = ListFollowAdapter(listFollow)
            binding.apply {
                rvFollow.adapter = adapter
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}