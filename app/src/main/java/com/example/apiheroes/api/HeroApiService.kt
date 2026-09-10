package com.example.apiheroes.api

import com.example.apiheroes.model.HeroApiResponse
import okhttp3.Response
import retrofit2.http.GET

interface HeroApiService {
    @GET("id/") //Endpoint
    suspend fun getHero(): Response<HeroApiResponse>

    @GET("all.json") //Endpoint
    suspend fun getAll(): Response<HeroApiResponse>
}