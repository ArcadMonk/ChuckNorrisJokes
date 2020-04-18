package com.example.chucknorrisjokes

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*

private val LOGTAG = "MyActivity/JokeAdapter"

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

    class JokeViewHolder( val jokeView: JokeView): ViewHolder(jokeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val jokeView = JokeView(parent.context, null,  0)
        return JokeViewHolder(jokeView)

    }
    override fun getItemCount(): Int {

        return jokes.size
    }
    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        if (position == jokes.size - 1){
            onBottomReachedListener?.onBottomReached(position);
        }
        var newJokeView = JokeView.Model(jokes[position].value, false)
        holder.jokeView.setupView(newJokeView)

        holder.jokeView.favButton.setOnClickListener {
            if (holder.jokeView.favState) {
                newJokeView = JokeView.Model(jokes[position].value, false)
                Log.d(LOGTAG, "joke unsaved")
            }
            else {
                newJokeView = JokeView.Model(jokes[position].value, true)
                Log.d(LOGTAG, "joke saved")
            }

            holder.jokeView.setupView(newJokeView)
        }
        holder.jokeView.shareButton.setOnClickListener {
            Log.d(LOGTAG, "joke shared")
        }

    }

    fun setBottomReachedL(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    fun moveJoke(indexFrom : Int, indexTo : Int) : Boolean{
        val joke = jokes[indexFrom]
        if (indexFrom < indexTo) {
            for (i in indexFrom until indexTo) {
                Collections.swap(jokes, i, i + 1)
            }
        } else {
            for (i in indexFrom downTo indexTo + 1) {
                Collections.swap(jokes, i, i - 1)
            }
        }
        Log.d(LOGTAG, "jokes at "+indexFrom+" moved to "+indexTo)

        return joke == jokes[indexTo]
    }

    fun deleteJoke(index : Int){
        jokes = jokes.subList(0, index).plus(jokes.subList(index+1, jokes.size))
        Log.d(LOGTAG, "deleted joke : "+jokes[index].value)
    }


}



