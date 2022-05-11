package com.example.newsclient.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiTest {

    private lateinit var service: NewsApi
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    private fun enqueueMockResponse(
        fileName: String
    ) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getTopHeadlines_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getNewsTopHeadlines("bitcoin","2394d5b6f3fa4d62a03c7d5009f0a080")
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/v2/everything?q=bitcoin&apiKey=2394d5b6f3fa4d62a03c7d5009f0a080")

        }
    }

    @Test
    fun getTopHeadlines_receivedResponse_correctContent() {
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getNewsTopHeadlines("bitcoin","2394d5b6f3fa4d62a03c7d5009f0a080")
            val articlesList = responseBody.articles
            val article = articlesList[0]
            assertThat(article.author).isEqualTo("Arielle Pardes")
            assertThat(article.url).isEqualTo("https://www.wired.com/story/bitcoin-2022-conference-harassment/")
            assertThat(article.publishedAt).isEqualTo("2022-05-10T16:59:46Z")
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

}