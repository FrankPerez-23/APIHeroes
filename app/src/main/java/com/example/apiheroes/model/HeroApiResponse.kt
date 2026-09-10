package com.example.apiheroes.model

import com.google.gson.annotations.SerializedName

data class HeroApiResponse(
    @SerializedName("message")
    val imageUrl: String,
    val status: String
)
