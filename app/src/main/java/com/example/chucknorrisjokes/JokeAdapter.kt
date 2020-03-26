package com.example.chucknorrisjokes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.*
import org.w3c.dom.Text

class JokeAdapter (  jokes:List<String> = emptyList() ) : Adapter <JokeAdapter.JokeViewHolder>(){

    class JokeViewHolder( val textJoke: TextView): ViewHolder(textJoke)

    var jokes =jokes
        set(value) {
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        //regarder google recycleview liste android devloper...*******************************************
        val textJoke = LayoutInflater.from(parent.context)
            .inflate(R.layout.joke_layout, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters
        return JokeViewHolder(textJoke)



    }

    override fun getItemCount(): Int {

        return JokeList.jokes.size
    }
    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.textJoke.text = jokes[position]

    }



}



