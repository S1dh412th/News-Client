package com.example.newsclient.data.api

import com.example.newsclient.data.model.NewsAPIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    suspend fun getNewsTopHeadlines(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ): NewsAPIResponse

}