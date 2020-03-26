package com.example.chucknorrisjokes

@Serializable
class Joke (


    val categories:List<String> = listOf<String> (),
    @SerialName("created_at")
    val creatAt: String,
    @SerialName("icon_url")
    val iconUrl: String,
    val id: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val url: String,
    val value: String
) {}

annotation class SerialName(val value: String)
