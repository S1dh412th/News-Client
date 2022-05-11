package com.example.newsclient.data.repository

import com.example.newsclient.data.model.NewsAPIResponse

interface NewsRepository {

    suspend fun getNewsTopHeadlines(q: String, apiKey: String) : NewsAPIResponse

}