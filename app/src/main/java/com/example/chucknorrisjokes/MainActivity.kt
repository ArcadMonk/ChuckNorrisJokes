package com.example.chucknorrisjokes

import android.content.Context
import android.content.SharedPreferences
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.util.concurrent.TimeUnit



private val LOGTAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPref: SharedPreferences
    private lateinit var savedAdapter: JokeAdapter

    private var initFavList: List<Joke> = emptyList()
    private val cmpsitDisposbl = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref= getSharedPreferences("Favorite Joke", Context.MODE_PRIVATE)
        sharedPref.all.forEach {
            initFavList = initFavList.plus(
                Json(JsonConfiguration.Stable).parse(Joke.serializer(), it.value.toString()
                )
            )
        }
        sharedPref.edit().apply { clear() }.apply()



        viewManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.pregressBar_id)

        val viewAdapter: JokeAdapter = JokeAdapter(initFavList)
        //load the save
        savedAdapter = viewAdapter


        recyclerView = findViewById<RecyclerView>(R.id.recvlerview_id).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        //At view bottom add more jokes :
        viewAdapter.setBottomReachedL(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                addToViewAdapter(viewAdapter)
                //To test functioning viewAdapter update :
                Log.d("PRINTING", viewAdapter.jokes.size.toString())
            }
        })

        val callback = JokeTouch({ mindex -> viewAdapter.deleteJoke(mindex) },
            { mindexFrom, mindexTo -> viewAdapter.moveJoke(mindexFrom, mindexTo) })

        callback.attachToRecyclerView(recyclerView)

        addToViewAdapter(viewAdapter)
    } // fin onCreate

    fun addToViewAdapter(tempViewAdapter: JokeAdapter) {

        val jokeFactory: JokeApiServiceFactory = JokeApiServiceFactory
        val sglJoke = jokeFactory.objFuncJkFactory().giveMeAJoke().repeat(2)
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.setVisibility(View.VISIBLE) }
            .doOnNext {}
            .doAfterTerminate {
                cmpsitDisposbl.clear()
            }
            .doOnTerminate {
                progressBar.setVisibility(View.INVISIBLE)
            }
            .subscribeBy(
                onError = { Log.d("Joke (ERROR) :", "$it") },
                onNext = {
                    savedAdapter.jokes = savedAdapter.jokes.plus(it)
                }
            )
        cmpsitDisposbl.add(sglJoke)
    }// fin addToViewAdapter


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        var atempJokeList = emptyList<Joke>()
        var saveString: String
        (0 until savedInstanceState.getInt("Size") - 1).forEachIndexed { index, _ ->
            try {
                saveString = savedInstanceState.getString("Joke$index")!!
                atempJokeList = atempJokeList.plus(Json(JsonConfiguration.Stable).parse(Joke.serializer(), saveString))
            }
            catch (e : Exception){}

        }
        savedAdapter.jokes = savedAdapter.jokes.plus(atempJokeList)
    }//fin onRestoreInstanceState

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("Size", savedAdapter.jokes.size)
        savedAdapter.jokes.forEachIndexed {index,joke ->
            if (!joke.favState) {
                outState.putString("Joke$index", Json(JsonConfiguration.Stable).stringify(Joke.serializer(), joke))
            }
        }
    }
    override fun onStop() {
        super.onStop()
        sharedPref.edit().apply {
            savedAdapter.favList.forEachIndexed {index,joke->
                putString("Joke$index", Json(JsonConfiguration.Stable).stringify(Joke.serializer(), joke))
            }
        }.apply()
    }

}




