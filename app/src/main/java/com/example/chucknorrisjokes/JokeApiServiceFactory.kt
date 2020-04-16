package com.example.chucknorrisjokes

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import retrofit2.Call as Call

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create



@UnstableDefault
object JokeApiServiceFactory  {
    // create an instance of JokeApiService
    fun objFuncJkFactory () :JokeApiService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/jokes/random/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


        return retrofit.create()
    }

}