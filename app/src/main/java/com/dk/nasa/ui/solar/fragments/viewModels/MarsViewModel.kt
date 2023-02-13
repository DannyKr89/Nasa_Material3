package com.dk.nasa.ui.solar.fragments.viewModels

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dk.nasa.BuildConfig
import com.dk.nasa.model.marsRover.MarsData
import com.dk.nasa.model.marsRover.MarsRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class MarsViewModel(
    private val liveData: MutableLiveData<MarsData> = MutableLiveData(),
    private val repositoryImpl: MarsRepositoryImpl = MarsRepositoryImpl()
) : ViewModel() {

    private var date: String

    init {
        date = LocalDate.now().toString()
    }

    fun setDate(days: Long) {
        date = LocalDate.now().minusDays(days).toString()
    }

    fun getLiveData() = liveData

    fun sendRequest() {
        repositoryImpl.getMarsApi().getMarsRoverData(date, BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<MarsData> {
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<MarsData>, response: Response<MarsData>
                ) {
                    val marsData = response.body()
                    if (response.isSuccessful && marsData != null) {
                        liveData.postValue(marsData)
                    }

                }

                override fun onFailure(call: Call<MarsData>, t: Throwable) {
                }
            })
    }
}