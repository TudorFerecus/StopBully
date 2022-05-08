package com.example.bullyapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api : APIService by lazy{
        Retrofit.Builder()
            .baseUrl("https://stopbully.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

}