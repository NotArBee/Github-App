package com.ardev.githubapp.ui.activity.favoriteuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardev.githubapp.database.FavoriteUser
import com.ardev.githubapp.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        showLoading(true)
        return mFavoriteUserRepository.getAllFavoriteUsers().apply {
            observeForever {
                showLoading(false)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}