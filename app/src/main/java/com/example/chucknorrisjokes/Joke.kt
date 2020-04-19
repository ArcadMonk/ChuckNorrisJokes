package com.example.chucknorrisjokes

import android.util.Log
import kotlinx.serialization.*
import java.lang.Exception

@Serializable
 data class Joke(val categories:List<String>,
           @SerialName("created_at")
           val createdAt:String,
           @SerialName("icon_url")
           val iconUrl:String,
           val id:String,
           @SerialName("updated_at")
           val updatedAt:String,
           val url:String,
           val value:String){
    var favState : Boolean = false
}