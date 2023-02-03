package com.dk.nasa.model.epic

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EpicAPI {

    @GET("api/natural")
    fun getEpicData(
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ): Call<EpicData>
}