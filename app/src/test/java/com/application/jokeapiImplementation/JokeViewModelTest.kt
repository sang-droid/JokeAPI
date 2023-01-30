package com.application.jokeapiImplementation

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.application.jokeapiImplementation.model.JokeResponse
import com.application.jokeapiImplementation.networkManager.JokeApi
import com.application.jokeapiImplementation.viewModel.JokeViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class JokeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel : JokeViewModel
    private val server = MockWebServer()
    private val context =  mock(Application::class.java)
    private lateinit var mockedResponse: String

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    @Before
    fun init(): Retrofit {
        viewModel = JokeViewModel(context)
        server.start(8000)

        var BASE_URL = "https://v2.jokeapi.dev/joke/Any"

        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        return service
    }

    @Test
    fun testApiSuccess() {
        mockedResponse = "{\n" +
                "    \"error\": false,\n" +
                "    \"category\": \"Pun\",\n" +
                "    \"type\": \"twopart\",\n" +
                "    \"setup\": \"You see, mountains aren't just funny.\",\n" +
                "    \"delivery\": \"They are hill areas.\",\n" +
                "    \"flags\": {\n" +
                "        \"nsfw\": false,\n" +
                "        \"religious\": false,\n" +
                "        \"political\": false,\n" +
                "        \"racist\": false,\n" +
                "        \"sexist\": false,\n" +
                "        \"explicit\": false\n" +
                "    },\n" +
                "    \"safe\": true,\n" +
                "    \"id\": 270,\n" +
                "    \"lang\": \"en\"\n" +
                "}"

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )

        val response = runBlocking { viewModel.randomJokeAPICall() }
        val json = gson.toJson(response)

        val resultResponse = JsonParser.parseString(json)
        val expectedresponse = JsonParser.parseString(mockedResponse)

        Assert.assertNotNull(response)
        Assert.assertTrue(resultResponse.equals(expectedresponse))
    }

}