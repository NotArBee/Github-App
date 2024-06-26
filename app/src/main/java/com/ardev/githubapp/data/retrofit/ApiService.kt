package com.ardev.githubapp.data.retrofit

import com.ardev.githubapp.data.response.DetailUserResponse
import com.ardev.githubapp.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getListUsers(
        @Query("q") q: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String): Call<List<DetailUserResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String): Call<List<DetailUserResponse>>
}