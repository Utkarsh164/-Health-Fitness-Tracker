package com.example.fitnessapp.API

import com.example.easytuto.API.MusicApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance
{
    fun  getInstance():Retrofit
    {
        return Retrofit.Builder()
            .baseUrl(Constant.burl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val MusicAPI : MusicApi = getInstance().create(MusicApi::class.java)
}