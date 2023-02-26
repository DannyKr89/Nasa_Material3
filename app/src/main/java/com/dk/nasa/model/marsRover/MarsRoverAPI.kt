package com.dk.nasa.model.marsRover

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsRoverAPI {
    @GET("mars-photos/api/v1/rovers/curiosity/photos?")
    fun getMarsRoverData(
        @Query("earth_date") date: String,
        @Query("api_key") apiKey: String
    ): Call<MarsData>
}