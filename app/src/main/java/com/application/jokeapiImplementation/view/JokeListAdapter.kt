package com.application.jokeapiImplementation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.jokeapiImplementation.R
import com.application.jokeapiImplementation.databinding.ShowJokesBinding
import com.application.jokeapiImplementation.model.Jokes


class JokeListAdapter :
    RecyclerView.Adapter<JokeListAdapter.MainActivityAdapterHolder>() {
    private var jokeList = ArrayList<Jokes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityAdapterHolder {
        return MainActivityAdapterHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.show_jokes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainActivityAdapterHolder, position: Int) {
        if(jokeList[position].type=="single"){
            holder.binding.tvSetup.visibility = View.GONE
            holder.binding.tvDelivery.visibility = View.GONE
            holder.binding.tvJokes.visibility = View.VISIBLE
            holder.binding.tvJokes.text = "${jokeList[position].joke}"
        }else {
            holder.binding.tvJokes.visibility = View.GONE
            holder.binding.tvSetup.visibility = View.VISIBLE
            holder.binding.tvDelivery.visibility = View.VISIBLE
            holder.binding.tvSetup.text = "${jokeList[position].setup}"
            holder.binding.tvDelivery.text = "${jokeList[position].delivery}"
        }

    }

    override fun getItemCount(): Int {
        return jokeList.size
    }

    class MainActivityAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ShowJokesBinding.bind(itemView)
    }

    fun updateList(jokeList: ArrayList<Jokes>, oldCount: Int, jokeSize: Int) {
        this.jokeList = jokeList
        notifyItemRangeInserted(oldCount, jokeSize)
    }
}