package com.ardev.githubapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ardev.githubapp.databinding.FragmentFollowBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.githubapp.ui.adapter.ListFollowAdapter
import com.ardev.githubapp.ui.detail.DetailViewModel
import com.ardev.githubapp.ui.main.MainActivity

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val githubUser = arguments?.getString(MainActivity.EXTRA_DATA)
        username = arguments?.getString(ARG_USERNAME)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.listFollow.observe(viewLifecycleOwner) { listFollow ->
            listFollow?.let { users ->
                binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
                val adapter = ListFollowAdapter(users)
                binding.rvFollow.adapter = adapter
            }
        }

        arguments?.let {
            position = it.getInt(ARG_SECTION_NUMBER)
            username = it.getString(ARG_USERNAME)
        }

        if (!githubUser.isNullOrEmpty()) {
            if (position == 1) {
                viewModel.getFollowersList(username!!)
            } else {
                viewModel.getFollowingList(username!!)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}




