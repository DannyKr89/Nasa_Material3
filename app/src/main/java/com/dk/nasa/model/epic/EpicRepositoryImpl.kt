package com.dk.nasa.model.epic

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EpicRepositoryImpl {
    private val baseUrl = "https://epic.gsfc.nasa.gov/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getEpicApi(): EpicAPI {
        return retrofit.create(EpicAPI::class.java)
    }
}