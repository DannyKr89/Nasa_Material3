package com.dk.nasa.ui.solar.fragments.viewModels

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dk.nasa.BuildConfig
import com.dk.nasa.model.epic.EpicData
import com.dk.nasa.model.epic.EpicRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class EarthViewModel(
    private val liveData: MutableLiveData<EpicData> = MutableLiveData(),
    private val repositoryImpl: EpicRepositoryImpl = EpicRepositoryImpl()
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
        repositoryImpl.getEpicApi().getEpicData(date, BuildConfig.NASA_API_KEY)
            .enqueue(object : Callback<EpicData> {
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<EpicData>,
                    response: Response<EpicData>
                ) {
                    val epicData = response.body()
                    if (response.isSuccessful && epicData != null) {
                        liveData.postValue(epicData)
                    }

                }

                override fun onFailure(call: Call<EpicData>, t: Throwable) {
                }
            })
    }
}