package com.application.jokeapiImplementation.networkManager

import com.application.jokeapiImplementation.model.JokeErrorResponse
import com.application.jokeapiImplementation.model.JokeListResponse
import com.application.jokeapiImplementation.model.JokeResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.Locale.Category


interface JokeApi {

    @GET("Any")
    fun getRandomJoke(@Query("blacklistFlags") type:String): Call<JokeResponse>
    @GET("Any")
    fun getJokes(@Query("blacklistFlags") flag:String,@Query("amount") page:Int): Call<JokeListResponse>
    @GET("Any")
    fun searchJoke(@Query("contains") key:String): Call<Any>

    @GET("{category}")
    fun categoryJokes(@Path("category")category : String): Call<JokeResponse>
}
