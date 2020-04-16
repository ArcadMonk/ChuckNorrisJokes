package com.example.chucknorrisjokes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chucknorrisjokes.JokeList.jokes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

private val LOGTAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val cmpsitDisposbl= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jokeFactory:JokeApiServiceFactory = JokeApiServiceFactory

        val sglJoke = jokeFactory.objFuncJkFactory().giveMeAJoke()
            .subscribeOn(Schedulers.io())
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError={Log.d("Joke :", "$it")},
                onSuccess={Log.d("Joke :", it.value)}
            )
        cmpsitDisposbl.add(sglJoke)


        if (!cmpsitDisposbl.isDisposed())
           cmpsitDisposbl.clear()



        viewManager = LinearLayoutManager(this)

        viewAdapter = JokeAdapter(jokes)

        recyclerView = findViewById<RecyclerView>(R.id.recvlerview_id).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        var testFirstJoke = JokeList.jokes[0].value
        Log.d( LOGTAG , JokeList.jokes[0].value)
    }
}


