package com.application.jokeapiImplementation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.jokeapiImplementation.model.JokeErrorResponse
import com.application.jokeapiImplementation.model.JokeResponse
import com.application.jokeapiImplementation.networkManager.JokeApi
import com.application.jokeapiImplementation.networkManager.WebServiceClient
import com.application.jokeapiImplementation.utility.NetworkConnection
import com.application.jokeapiImplementation.utility.SingleLiveEvent
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class SearchJokeViewModel(application: Application) : AndroidViewModel(application) {
    private val networkMonitor = NetworkConnection(application)
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var alertDialog: SingleLiveEvent<Boolean>? = null
    var errorDialog: SingleLiveEvent<String>? = null
    var joke: MutableLiveData<Any>? = null
    var jokeError: SingleLiveEvent<String>? = null

    init {
        progressDialog = SingleLiveEvent()
        alertDialog = SingleLiveEvent()
        errorDialog = SingleLiveEvent()
        joke = MutableLiveData<Any>()
        jokeError = SingleLiveEvent<String>()
    }
    fun searchJokeAPICall(searchKey : String) = if(networkMonitor.isNetworkAvailable()) {
        progressDialog?.value = true
        with(WebServiceClient) {
            client.create(JokeApi::class.java).searchJoke(searchKey)
                .enqueue(object : Callback<Any?> {
                    override fun onResponse(
                        call: Call<Any?>?,
                        response: Response<Any?>?
                    ) {
                        progressDialog?.value = false
                        if(response?.body()!=null){
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            var error = jsonObject.getBoolean("error")
                            if(error){
                                val gson = Gson()
                                val errorResponse = gson.fromJson(gson.toJson(response.body()), JokeErrorResponse::class.java)
                                jokeError?.value = errorResponse.message
                            }else{
                                val gson = Gson()
                                val response = gson.fromJson(gson.toJson(response.body()), JokeResponse::class.java)
                                joke?.value = response
                            }

                        }
                        else if(response?.errorBody()!=null){
                            try {
                                errorDialog?.value = response.errorBody().toString()
                            } catch (ex: IOException) {
                                ex.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Any?>?, t: Throwable?) {
                        progressDialog?.value = false
                    }
                })
        }
    }
    else{
        alertDialog?.value = true
    }
    fun jokeCategoryAPICall(category : String) = if(networkMonitor.isNetworkAvailable()) {
        progressDialog?.value = true
        with(WebServiceClient) {
            client.create(JokeApi::class.java).categoryJokes(category)
                .enqueue(object : Callback<JokeResponse?> {
                    override fun onResponse(
                        call: Call<JokeResponse?>?,
                        response: Response<JokeResponse?>?
                    ) {
                        progressDialog?.value = false
                        if(response?.body()!=null){
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            var error = jsonObject.getBoolean("error")
                            if(error){
                                val gson = Gson()
                                val errorResponse = gson.fromJson(gson.toJson(response.body()), JokeErrorResponse::class.java)
                                jokeError?.value = errorResponse.message
                            }else{
                                val gson = Gson()
                                val response = gson.fromJson(gson.toJson(response.body()), JokeResponse::class.java)
                                joke?.value = response
                            }

                        }
                        else if(response?.errorBody()!=null){
                            try {
                                errorDialog?.value = response.errorBody().toString()
                            } catch (ex: IOException) {
                                ex.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<JokeResponse?>?, t: Throwable?) {
                        progressDialog?.value = false
                    }
                })
        }
    }
    else{
        alertDialog?.value = true
    }
}