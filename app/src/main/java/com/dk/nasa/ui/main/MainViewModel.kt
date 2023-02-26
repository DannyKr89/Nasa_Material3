package com.dk.nasa.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dk.nasa.BuildConfig
import com.dk.nasa.model.pictureOfTheDay.PictureOfTheDayData
import com.dk.nasa.model.pictureOfTheDay.RepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    var date: String

    init {
        date = LocalDate.now().toString()
    }

    fun setDate(days: Long) {
        date = LocalDate.now().minusDays(days).toString()
    }

    fun getLiveData() = liveData

    fun sendRequest() {
        liveData.value = AppState.Loading
        repositoryImpl.getPictureOfTheDayApi().getPictureOfTheDay(date,BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<PictureOfTheDayData> {
                override fun onResponse(
                    call: Call<PictureOfTheDayData>,
                    response: Response<PictureOfTheDayData>
                ) {
                    val pictureOfTheDayData = response.body()
                    if (response.isSuccessful && pictureOfTheDayData != null){
                        liveData.postValue(AppState.Success(pictureOfTheDayData))
                    } else {
                        liveData.postValue(AppState.Error(response.errorBody().toString()))
                    }

                }

                override fun onFailure(call: Call<PictureOfTheDayData>, t: Throwable) {
                    liveData.postValue(AppState.Error(t.message.toString()))
                }
            })
    }
}