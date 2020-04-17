package com.example.chucknorrisjokes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


private val LOGTAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var progressBar: ProgressBar

    private val cmpsitDisposbl= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.pregressBar_id)

        var viewAdapter: JokeAdapter = JokeAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.recvlerview_id).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        addToViewAdapter(viewAdapter)

        viewAdapter.setBottomReachedL(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                addToViewAdapter(viewAdapter)
                //To test fonctionning viewAdapter update :
                Log.d("PRINTING",viewAdapter.jokes.size.toString())

            }
        })





    }

    fun addToViewAdapter(tempViewAdapter : JokeAdapter){

        var joke: Joke
        val jokeFactory:JokeApiServiceFactory = JokeApiServiceFactory
        val sglJoke = jokeFactory.objFuncJkFactory().giveMeAJoke().repeat(2)
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{progressBar.setVisibility(View.VISIBLE)}
            .doOnNext{}
            .doAfterTerminate {
                cmpsitDisposbl.clear()
            }
            .doOnTerminate{
                progressBar.setVisibility(View.INVISIBLE)
            }
            .subscribeBy(
                onError={Log.d("Joke :", "$it")},
                onNext={
                    tempViewAdapter.jokes = tempViewAdapter.jokes.plus(it)

                }
            )


        cmpsitDisposbl.add(sglJoke)


    }
}


