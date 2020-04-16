package com.example.chucknorrisjokes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chucknorrisjokes.JokeList.jokes
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe
import io.reactivex.rxkotlin.subscribeBy

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import retrofit2.Call as Call

private val LOGTAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jokeFactory:JokeApiServiceFactory = JokeApiServiceFactory

        val sglJoke = jokeFactory.objFuncJkFactory().giveMeAJoke().subscribeBy(
            onError={Log.d("ERROR",it.toString())},
            onSuccess={Log.d(LOGTAG,it.toString())}
        )

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


