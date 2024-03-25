package com.ardev.githubapp.data.retrofit

import com.ardev.githubapp.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getListUsers(
        @Query("q") q: String
    ): Call<GithubResponse>
}