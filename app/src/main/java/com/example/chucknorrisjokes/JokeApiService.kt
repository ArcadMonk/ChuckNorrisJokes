package com.example.chucknorrisjokes

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET


public interface JokeApiService {

    @GET("https://api.chucknorris.io/jokes/random/")
    fun giveMeAJoke():Single<Joke>
    fun giveMeJokes():Observable<Single<Joke>>

}