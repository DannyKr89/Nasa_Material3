package com.dk.nasa.ui.solar.fragments.viewModels

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dk.nasa.BuildConfig
import com.dk.nasa.model.marsRover.MarsData
import com.dk.nasa.model.marsRover.MarsRepositoryImpl
import com.dk.nasa.model.photos.Photos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class MarsViewModel(
    private val liveData: MutableLiveData<MutableList<Photos>> = MutableLiveData(),
    private val repositoryImpl: MarsRepositoryImpl = MarsRepositoryImpl()
) : ViewModel() {

    private var date: String

    init {
        date = LocalDate.now().toString()
    }

    fun setDate(days: Long) {
        date = LocalDate.now().minusDays(days).toString()
    }

    fun swapItems(oldPOosition: Int, newPosition: Int) {
        Collections.swap(liveData.value!!,oldPOosition,newPosition)
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
                        val list = convertToPhotos(marsData)
                        liveData.postValue(list)
                    }

                }

                override fun onFailure(call: Call<MarsData>, t: Throwable) {
                }
            })
    }
    private fun convertToPhotos(marsData: MarsData): MutableList<Photos> {
        val mutableList = mutableListOf<Photos>()
        marsData.photos.forEach {
            mutableList.add(
                Photos(
                    image = it.img_src,
                    description = it.camera.full_name,
                    date = it.earth_date
                )
            )
        }
        return mutableList
    }
}
