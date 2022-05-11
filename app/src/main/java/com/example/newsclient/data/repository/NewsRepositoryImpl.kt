package com.example.newsclient.data.repository

import com.example.newsclient.data.api.NewsApi
import com.example.newsclient.data.model.NewsAPIResponse

class NewsRepositoryImpl(
    private val newsApi: NewsApi
): NewsRepository {

    override suspend fun getNewsTopHeadlines(q: String, apiKey: String) : NewsAPIResponse{
        return newsApi.getNewsTopHeadlines(q = q, apiKey = apiKey)
    }

}