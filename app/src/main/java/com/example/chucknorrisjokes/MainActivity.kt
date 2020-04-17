package com.example.chucknorrisjokes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


private val LOGTAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val cmpsitDisposbl= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)

        var viewAdapter = JokeAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.recvlerview_id).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        addButton_id.setOnClickListener{addToViewAdapter(viewAdapter)}

    }

    fun addToViewAdapter(tempViewAdapter : JokeAdapter){

        var joke: Joke
        val jokeFactory:JokeApiServiceFactory = JokeApiServiceFactory
        val sglJoke = jokeFactory.objFuncJkFactory().giveMeAJoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError={Log.d("Joke :", "$it")},
                onSuccess={
                    tempViewAdapter.jokes = tempViewAdapter.jokes.plus(it)
                }

            )
        cmpsitDisposbl.add(sglJoke)

        //if (!cmpsitDisposbl.isDisposed())
        //    cmpsitDisposbl.clear()
    }
}


