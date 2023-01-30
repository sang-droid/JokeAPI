package com.application.jokeapiImplementation.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.jokeapiImplementation.R
import com.application.jokeapiImplementation.databinding.ActivityJokeListBinding
import com.application.jokeapiImplementation.model.Jokes
import com.application.jokeapiImplementation.utility.AlertDialogHelper
import com.application.jokeapiImplementation.viewModel.JokeListViewModel

class JokeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJokeListBinding
    private lateinit var jokeListAdapter: JokeListAdapter
    private lateinit var viewModel : JokeListViewModel

    private var currentPage = 10
    private var totalAvailablePages = 319
    private lateinit var jokes: ArrayList<Jokes>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this)[JokeListViewModel::class.java]
        initViews()
        viewModel.jokes?.observe(this) {
            val oldCount = jokes.size
            jokes.addAll(it)
            jokeListAdapter.updateList(jokes, oldCount, jokes.size)
            toogleLoading()
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
    private fun initViews() {
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        jokes = ArrayList()
        jokeListAdapter = JokeListAdapter()
        binding.recyclerview.adapter = jokeListAdapter
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.recyclerview.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 10
                        loadPageList()
                    }
                }
            }
        })
        loadPageList()
    }
    private fun loadPageList() {
        toogleLoading()
        viewModel.jokeListAPICall(currentPage)
    }

    private fun toogleLoading() {
        if (currentPage == 10) {
            if (binding.defaultProgress.isShown) {
                binding.defaultProgress.visibility = View.GONE
            } else {
                binding.defaultProgress.visibility = View.VISIBLE
            }
        } else {
            if (binding.loadMoreProgress.isShown) {
                binding.loadMoreProgress.visibility = View.GONE
            } else {
                binding.loadMoreProgress.visibility = View.VISIBLE
            }
        }
    }
}