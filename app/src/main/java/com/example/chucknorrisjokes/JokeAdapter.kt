package com.example.chucknorrisjokes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface OnBottomReachedListener {
    fun onBottomReached(position: Int)
}

class JokeAdapter() : Adapter <JokeAdapter.JokeViewHolder>(){


    var onBottomReachedListener: OnBottomReachedListener? = null

    var jokes = emptyList<Joke>()
        set(value) {
            field=value
            notifyDataSetChanged()
        }

    class JokeViewHolder( val textJoke: TextView): ViewHolder(textJoke)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val textJoke = LayoutInflater.from(parent.context)
            .inflate(R.layout.joke_layout, parent, false) as TextView
        return JokeViewHolder(textJoke)

    }
    override fun getItemCount(): Int {

        return jokes.size
    }
    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        if (position == jokes.size - 1){
            onBottomReachedListener?.onBottomReached(position);
        }

        holder.textJoke.text = jokes[position].value

    }

    fun setBottomReachedL(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }

}



