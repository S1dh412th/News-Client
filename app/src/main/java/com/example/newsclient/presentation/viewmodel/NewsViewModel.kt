package com.example.newsclient.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsclient.data.Instance
import com.example.newsclient.data.LocalDataSourceInstance
import com.example.newsclient.data.model.Article
import com.example.newsclient.data.model.NewsAPIResponse
import com.example.newsclient.data.repository.NewsLocalDataSource
import com.example.newsclient.data.repository.NewsLocalDataSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val app: Application
) : AndroidViewModel(app) {

    private val newsRepository = Instance.getNewsRepository()
    private val localRepository: NewsLocalDataSource

    private var sharedPreferences: SharedPreferences =
        app.getSharedPreferences("credentials", Context.MODE_PRIVATE)

    private var apikey: String

    private val newsMutableFlow = MutableStateFlow<NewsAPIResponse?>(null)
    val newsFlow: Flow<NewsAPIResponse?> = newsMutableFlow

    init {

        val dao = LocalDataSourceInstance.getDatabase(app).getArticleDAO()
        localRepository = NewsLocalDataSourceImpl(dao)

        apikey = sharedPreferences.getString("news_api_key", "").toString()

        viewModelScope.launch {
            if (checkForInternet()) {
                newsMutableFlow.value = newsRepository.getNewsTopHeadlines("bitcoin", apikey)
            } else {
                Toast.makeText(app, "Internet Connection is not there", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        localRepository.saveArticleToDB(article)
    }

    fun getSavedNews() = liveData {
        localRepository.getSavedArticles().collect {
            emit(it)
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        localRepository.deleteArticlesFromDB(article)
    }

    fun updateNews(q: String) = viewModelScope.launch {
        if (checkForInternet()) {
            newsMutableFlow.value = newsRepository.getNewsTopHeadlines(q, apikey)
        } else {
            Toast.makeText(app, "Internet Connection is not there", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkForInternet(): Boolean {

        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}