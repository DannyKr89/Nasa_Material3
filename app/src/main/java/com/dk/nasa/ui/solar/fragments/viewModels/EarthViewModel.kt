package com.dk.nasa.ui.solar.fragments.viewModels

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dk.nasa.BuildConfig
import com.dk.nasa.model.epic.EpicData
import com.dk.nasa.model.epic.EpicRepositoryImpl
import com.dk.nasa.model.photos.Photos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class EarthViewModel(
    private val liveData: MutableLiveData<MutableList<Photos>> = MutableLiveData(),
    private val repositoryImpl: EpicRepositoryImpl = EpicRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveData

    fun sendRequest() {
        repositoryImpl.getEpicApi().getEpicData(BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<EpicData> {
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<EpicData>,
                    response: Response<EpicData>
                ) {
                    val epicData = response.body()
                    if (response.isSuccessful && epicData != null) {
                        val list = convertToPhotos(epicData)
                        liveData.postValue(list)
                    }

                }

                override fun onFailure(call: Call<EpicData>, t: Throwable) {
                }
            })
    }

    private fun convertToPhotos(epicData: EpicData): MutableList<Photos> {
        val year = epicData.first().date.substring(0, 4)
        val month = epicData.first().date.substring(5, 7)
        val day = epicData.first().date.substring(8, 10)
        val mutableList = mutableListOf<Photos>()
        epicData.forEach {
            mutableList.add(
                Photos(
                    image = "https://epic.gsfc.nasa.gov/archive/natural/$year/$month/$day/png/${it.image}.png",
                    description = it.caption,
                    date = it.date
                )
            )
        }
        return mutableList
    }
}