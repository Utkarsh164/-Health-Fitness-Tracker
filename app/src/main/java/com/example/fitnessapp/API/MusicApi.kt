package com.example.easytuto.API

import com.example.fitnessapp.API.MyData
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface MusicApi {
  @GET("search")
  @Headers(
      "X-RapidAPI-Key: f7ca6ae89emsh61758bef72ad2f3p161945jsn842b4e86326d",
      "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"
  )
    suspend fun getMusic(
       //@Query("key") apiKey : String,
        @Query("q") query : String
    ) : Response<MyData> }



