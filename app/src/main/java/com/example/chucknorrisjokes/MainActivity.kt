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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.util.concurrent.TimeUnit



private val LOGTAG = "MyActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var progressBar: ProgressBar

    private lateinit var savedAdapter:JokeAdapter
    private lateinit var savedJokes:List<Joke>

    private val cmpsitDisposbl= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.pregressBar_id)

        val viewAdapter: JokeAdapter = JokeAdapter()
        //load the save
        savedAdapter = viewAdapter
        var mListState = viewManager.onSaveInstanceState();


        recyclerView = findViewById<RecyclerView>(R.id.recvlerview_id).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //Start the app this 2 jokes :
        addToViewAdapter(viewAdapter)
        //At view bottom add more jokes :
        viewAdapter.setBottomReachedL(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                addToViewAdapter(viewAdapter)
                //To test functioning viewAdapter update :
                Log.d("PRINTING",viewAdapter.jokes.size.toString())
            }
        })
    } // fin onCreate

    fun addToViewAdapter(tempViewAdapter : JokeAdapter){

        val jokeFactory:JokeApiServiceFactory = JokeApiServiceFactory
        val sglJoke = jokeFactory.objFuncJkFactory().giveMeAJoke().repeat(10)
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
                onNext= {
                    tempViewAdapter.jokes = tempViewAdapter.jokes.plus(it)
                    savedJokes = tempViewAdapter.jokes
                }
            )
        cmpsitDisposbl.add(sglJoke)
    }// fin addToViewAdapter



    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        var atempJokeList = emptyList<Joke>()
        var saveString : String
        (0..savedInstanceState.getInt("Size")-1).forEachIndexed { index, _ ->
            saveString = savedInstanceState.getString("Joke$index")!!
            atempJokeList = atempJokeList.plus(Json(JsonConfiguration.Stable).parse(Joke.serializer(), saveString))
        }
        savedAdapter.jokes = savedAdapter.jokes.plus(atempJokeList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("Size", savedJokes.size)
        savedJokes.forEachIndexed { index,
                                    it ->
            outState.putString(
                "Joke$index",
                Json(JsonConfiguration.Stable).stringify(Joke.serializer(), it)
            )
        }
    }
}





