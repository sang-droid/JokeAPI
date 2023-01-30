package com.application.jokeapiImplementation.view

import android.annotation.SuppressLint
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
import com.application.jokeapiImplementation.databinding.ActivitySearchJokeBinding
import com.application.jokeapiImplementation.model.JokeResponse
import com.application.jokeapiImplementation.utility.AlertDialogHelper
import com.application.jokeapiImplementation.utility.CustomProgressDialog
import com.application.jokeapiImplementation.viewModel.SearchJokeViewModel

class SearchJokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchJokeBinding
    private lateinit var viewModel : SearchJokeViewModel
    private lateinit var selectedCategory : ArrayList<String>
    private var customProgressDialog: CustomProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchJokeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customProgressDialog = CustomProgressDialog(this)
        selectedCategory = ArrayList()
        viewModel = ViewModelProvider(this)[SearchJokeViewModel::class.java]
        binding.searchBtn.setOnClickListener {
            if (!binding.checkBox1.isChecked && !binding.checkBox2.isChecked && !binding.checkBox3.isChecked
                && !binding.checkBox4.isChecked && !binding.checkBox5.isChecked && !binding.checkBox6.isChecked) {
                binding.checkBox1.isChecked = false
                binding.checkBox2.isChecked = false
                binding.checkBox3.isChecked = false
                binding.checkBox4.isChecked = false
                binding.checkBox5.isChecked = false
                binding.checkBox6.isChecked = false
                viewModel.searchJokeAPICall(binding.etSearch.text.toString())
            } else {
                viewModel.jokeCategoryAPICall(selectedCategory.joinToString(separator = ","))

            }
        }
        binding.checkBox1.setOnCheckedChangeListener { _, isCheckBox1Checked ->
            if (isCheckBox1Checked) {
                selectedCategory.add(binding.checkBox1.text.toString())
            }else{
                selectedCategory.remove(binding.checkBox1.text.toString())
            }
        }
        binding.checkBox2.setOnCheckedChangeListener { _, isCheckBox2Checked ->
            if (isCheckBox2Checked) {
                selectedCategory.add(binding.checkBox2.text.toString())
            }else{
                selectedCategory.remove(binding.checkBox2.text.toString())
            }
        }

        binding.checkBox3.setOnCheckedChangeListener { _, isCheckBox3Checked ->
            if (isCheckBox3Checked) {
                selectedCategory.add(binding.checkBox3.text.toString())
            }else{
                selectedCategory.remove(binding.checkBox3.text.toString())
            }
        }

            binding.checkBox4.setOnCheckedChangeListener { _, isCheckBox4Checked ->
                if (isCheckBox4Checked) {
                    selectedCategory.add(binding.checkBox4.text.toString())
                }else{
                    selectedCategory.remove(binding.checkBox4.text.toString())
                }
            }
            binding.checkBox5.setOnCheckedChangeListener { _, isCheckBox5Checked ->
                if (isCheckBox5Checked) {
                    selectedCategory.add(binding.checkBox5.text.toString())
                }else{
                    selectedCategory.remove(binding.checkBox5.text.toString())
                }
            }
            binding.checkBox6.setOnCheckedChangeListener { _, isCheckBox6Checked ->
                if (isCheckBox6Checked) {
                    selectedCategory.add(binding.checkBox6.text.toString())
                }else{
                    selectedCategory.remove(binding.checkBox6.text.toString())
                }
            }
        viewModel.progressDialog?.observe(this) {
            if (it!!) customProgressDialog?.show() else customProgressDialog?.dismiss()
        }
        viewModel.joke?.observe(this){
            showPopUp(it as JokeResponse?, "")
        }
        viewModel.jokeError?.observe(this){
            showPopUp(null, it)
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
        viewModel.errorDialog?.observe(this) {
            if (it.isNotEmpty()) {
                AlertDialogHelper.showAlert(this, getString(R.string.error), it,
                    object : AlertDialogHelper.Callback {
                        override fun onSucess(t: Int) {
                        }
                    })
            }
        }
    }
    @SuppressLint("InflateParams")
    private fun showPopUp(jokeResponse: JokeResponse?, message: String) {
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

        if(jokeResponse != null) {
                if (jokeResponse.type == getString(R.string.single)) {
                    joke.visibility = View.VISIBLE
                    joke.text = jokeResponse.joke
                    jokeSetup.visibility = View.GONE
                    jokeDelivery.visibility = View.GONE
                } else {
                    joke.visibility = View.GONE
                    jokeSetup.text = jokeResponse.setup
                    jokeDelivery.text = jokeResponse.delivery
                }
            }else{
                jokeSetup.visibility = View.GONE
                jokeDelivery.visibility = View.GONE
                joke.visibility = View.VISIBLE
                joke.text = message
            }
        closePopupBtn.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}