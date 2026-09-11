package com.example.apiheroes.network

import com.example.apiheroes.api.HeroApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://akabab.github.io/superhero-api/api/"

    val api: HeroApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeroApiService::class.java)
    }
}