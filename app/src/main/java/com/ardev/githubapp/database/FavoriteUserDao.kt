package com.ardev.githubapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser ORDER BY login ASC")
    fun getAllFavorites(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE login = :login")
    fun getFavoriteUserByUsername(login: String): LiveData<FavoriteUser>
}