package com.ardev.githubapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardev.githubapp.data.response.GithubResponse
import com.ardev.githubapp.data.response.ItemsItem
import com.ardev.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
        private const val USERNAME = "Arya"
    }

    private val _itemsItem = MutableLiveData<List<ItemsItem>>()
    val itemsItem: LiveData<List<ItemsItem>> = _itemsItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        showUsersList()
    }

    private fun showUsersList() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUsers(USERNAME)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _itemsItem.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message.toString()}")
            }
        })
    }

//    private fun showUsersSearch() {
//        _isLoading.value = false
//        val client = ApiConfig.getApiService().getListUsers(search)
//        client.enqueue(object : Callback<GithubResponse> {
//            override fun onResponse(
//                call: Call<GithubResponse>,
//                response: Response<GithubResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _itemsItem.value = response.body()?.items
//                } else {
//                    Log.e(TAG, "onFailure : ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(TAG, "onFailure : ${t.message.toString()}")
//            }
//        })
//    }
}