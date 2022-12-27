package com.dk.nasa.model

import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Callback<PictureOfTheDayData>
}