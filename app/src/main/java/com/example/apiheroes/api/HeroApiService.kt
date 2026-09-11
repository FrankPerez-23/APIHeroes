package com.example.apiheroes.api

import com.example.apiheroes.model.HeroApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroApiService {

    @GET("id/{id}.json") //Endpoint
    suspend fun getHero(@Path("id") id: Int): Response<HeroApiResponse>

    @GET("all.json") //Endpoint
    suspend fun getAll(): Response<List<HeroApiResponse>>
}