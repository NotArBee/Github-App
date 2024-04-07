package com.ardev.githubapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ardev.githubapp.database.FavoriteUser
import com.ardev.githubapp.database.FavoriteUserDao
import com.ardev.githubapp.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val exeutorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.FavoriteDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavorites()

    fun insert(favUser: FavoriteUser) {
        exeutorService.execute { mFavoriteUserDao.insert(favUser) }
    }

    fun update(favUser: FavoriteUser) {
        exeutorService.execute { mFavoriteUserDao.update(favUser) }
    }

    fun delete(favUser: FavoriteUser) {
        exeutorService.execute { mFavoriteUserDao.delete(favUser) }
    }
}