package com.application.jokeapiImplementation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.jokeapiImplementation.model.JokeListResponse
import com.application.jokeapiImplementation.model.JokeResponse
import com.application.jokeapiImplementation.model.Jokes
import com.application.jokeapiImplementation.networkManager.JokeApi
import com.application.jokeapiImplementation.networkManager.WebServiceClient
import com.application.jokeapiImplementation.utility.NetworkConnection
import com.application.jokeapiImplementation.utility.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class JokeListViewModel(application: Application) : AndroidViewModel(application) {
    private val networkMonitor = NetworkConnection(application)
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var alertDialog: SingleLiveEvent<Boolean>? = null
    var errorDialog: SingleLiveEvent<String>? = null
    var jokes: MutableLiveData<ArrayList<Jokes>>? = null

    init {
        progressDialog = SingleLiveEvent()
        alertDialog = SingleLiveEvent()
        errorDialog = SingleLiveEvent()
        jokes = MutableLiveData<ArrayList<Jokes>>()
    }
    fun jokeListAPICall(page: Int) = if(networkMonitor.isNetworkAvailable()) {
        println("Page is" + page)
        progressDialog?.value = true
        with(WebServiceClient) {
            client.create(JokeApi::class.java).getJokes("political", page)
                .enqueue(object : Callback<JokeListResponse?> {
                    override fun onResponse(
                        call: Call<JokeListResponse?>?,
                        response: Response<JokeListResponse?>?
                    ) {
                        progressDialog?.value = false
                        if(response?.body()!=null){
                            jokes?.value = response.body()!!.jokes
                        }
                        else if(response?.errorBody()!=null){
                            try {
                                errorDialog?.value = response.errorBody().toString()
                            } catch (ex: IOException) {
                                ex.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<JokeListResponse?>?, t: Throwable?) {
                        progressDialog?.value = false
                    }
                })
        }
    } else {
        alertDialog?.value = true
    }

}