package com.example.chucknorrisjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chucknorrisjokes.JokeList.jokes




private val LOGTAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)

        viewAdapter = JokeAdapter(jokes)

        recyclerView = findViewById<RecyclerView>(R.id.recvlerview_id).apply {
           setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter

        }


        var testFirstJoke = JokeList.jokes[0].value
        Log.d( LOGTAG , JokeList.jokes[0].value)

        //Log.d( LOGTAG , JokeList.jokes.toString())
    }
}


