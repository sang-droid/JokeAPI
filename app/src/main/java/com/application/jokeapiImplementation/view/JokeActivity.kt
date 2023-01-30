package com.application.jokeapiImplementation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.application.jokeapiImplementation.R
import com.application.jokeapiImplementation.databinding.ActivityJokeBinding
import com.application.jokeapiImplementation.model.JokeResponse
import com.application.jokeapiImplementation.utility.AlertDialogHelper
import com.application.jokeapiImplementation.utility.CustomProgressDialog
import com.application.jokeapiImplementation.viewModel.JokeViewModel

class JokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJokeBinding
    private lateinit var viewModel : JokeViewModel
    private var customProgressDialog: CustomProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customProgressDialog = CustomProgressDialog(this)
        viewModel =
            ViewModelProvider(this)[JokeViewModel::class.java]
        binding.randomJokeBtn.setOnClickListener{
            viewModel.randomJokeAPICall()
        }
        viewModel.progressDialog?.observe(this) {
            if (it!!) customProgressDialog?.show() else customProgressDialog?.dismiss()
        }
        viewModel.randomJoke?.observe(this) {
            showPopUp(it)
        }
        viewModel.alertDialog?.observe(this){
            if(it){
                AlertDialogHelper.showAlert(this,getString(R.string.networkError),getString(R.string.detailError),
                    object : AlertDialogHelper.Callback {
                        override fun onSucess(t: Int) {
                        }
                })
            }
        }
        viewModel.errorDialog?.observe(this){
            if(it.isNotEmpty()){
                AlertDialogHelper.showAlert(this,getString(R.string.error),it,
                    object : AlertDialogHelper.Callback {
                        override fun onSucess(t: Int) {
                        }
                    })
            }
        }
        binding.jokeListBtn.setOnClickListener {
            val intent = Intent(this ,JokeListActivity::class.java)
            startActivity(intent)

        }
        binding.textInputBtn.setOnClickListener {
            val intent = Intent(this ,SearchJokeActivity::class.java)
            startActivity(intent)

        }
        }
    @SuppressLint("InflateParams")
    private fun showPopUp(jokeResponse: JokeResponse) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup, null)

        val wid = LinearLayout.LayoutParams.WRAP_CONTENT
        val high = LinearLayout.LayoutParams.WRAP_CONTENT
        val focus = true
        //instantiate popup window
        val popupWindow = PopupWindow(popupView, wid, high, focus)

        //display the popup window
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val closePopupBtn = popupView.findViewById(R.id.closePopupBtn) as Button
        val joke = popupView.findViewById(R.id.joke) as TextView
        val jokeSetup = popupView.findViewById(R.id.jokeSetUp) as TextView
        val jokeDelivery = popupView.findViewById(R.id.jokeDelivery) as TextView

        if(jokeResponse.type == "single"){
            joke.visibility = View.VISIBLE
            joke.text = jokeResponse.joke
            jokeSetup.visibility = View.GONE
            jokeDelivery.visibility = View.GONE
        }else{
            joke.visibility = View.GONE
            jokeSetup.text = jokeResponse.setup
            jokeDelivery.text = jokeResponse.delivery
        }


        //close the popup window on button click
        closePopupBtn.setOnClickListener {
            popupWindow.dismiss()
        }

    }
    }


