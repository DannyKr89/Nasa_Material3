package com.dk.nasa.model.marsRover

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarsRepositoryImpl {
    private val baseUrl = "https://api.nasa.gov/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getMarsApi(): MarsRoverAPI {
        return retrofit.create(MarsRoverAPI::class.java)
    }
}