package com.example.chucknorrisjokes

import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*


private val LOGTAG = "MyActivity/JokeAdapter"

interface OnBottomReachedListener {
    fun onBottomReached(position: Int)
}

class JokeAdapter(favList: List<Joke>) : Adapter <JokeAdapter.JokeViewHolder>(){


    var favList = favList
    var onBottomReachedListener: OnBottomReachedListener? = null
    var jokes = favList
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

        var newJokeView = if(jokes[position].favState) {
           JokeView.Model(jokes[position].value, true)
        }
        else {
            JokeView.Model(jokes[position].value, false)
        }

        holder.jokeView.setupView(newJokeView)

        holder.jokeView.favButton.setOnClickListener {
            if (jokes[position].favState) {
                newJokeView = JokeView.Model(jokes[position].value, false)
                jokes[position].favState = false
                favList = favList.minus(jokes[position])
                Log.d(LOGTAG, "joke unsaved")
            }
            else {
                newJokeView = JokeView.Model(jokes[position].value, true)
                jokes[position].favState = true
                favList = favList.plus(jokes[position])
                //moveJoke(position,favList.size-1)
                Log.d(LOGTAG, "joke saved")
            }

            holder.jokeView.setupView(newJokeView)
        }
        holder.jokeView.shareButton.setOnClickListener {
            Log.d(LOGTAG, "show share options")
            val sharingIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, jokes[position].value)
            }, "Share via")
            it.getContext().startActivity(sharingIntent)
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

    fun deleteJoke(index : Int) {
        if (jokes[index].favState){
            Log.d(LOGTAG, "deleted favJoke : "+jokes[index].value)
            favList = favList.minus(jokes[index])
        }
        Log.d(LOGTAG, "deleted joke : "+jokes[index].value)
        jokes = jokes.minus(jokes[index])


    }


}



