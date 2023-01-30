package com.application.jokeapiImplementation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.jokeapiImplementation.model.JokeResponse
import com.application.jokeapiImplementation.networkManager.JokeApi
import com.application.jokeapiImplementation.networkManager.WebServiceClient
import com.application.jokeapiImplementation.utility.NetworkConnection
import com.application.jokeapiImplementation.utility.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class JokeViewModel(application: Application) : AndroidViewModel(application) {
    private val networkMonitor = NetworkConnection(application)
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var alertDialog: SingleLiveEvent<Boolean>? = null
    var errorDialog: SingleLiveEvent<String>? = null
    var randomJoke: MutableLiveData<JokeResponse>? = null

    init {
        progressDialog = SingleLiveEvent()
        alertDialog = SingleLiveEvent()
        errorDialog = SingleLiveEvent()
        randomJoke = MutableLiveData<JokeResponse>()
    }

    fun randomJokeAPICall() = if(networkMonitor.isNetworkAvailable()) {
        progressDialog?.value = true
        with(WebServiceClient) {
            client.create(JokeApi::class.java).getRandomJoke("political")
                .enqueue(object : Callback<JokeResponse?> {
                    override fun onResponse(
                        call: Call<JokeResponse?>?,
                        response: Response<JokeResponse?>?
                    ) {
                        progressDialog?.value = false
                        if(response?.body()!=null){
                         randomJoke?.value = response.body()
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