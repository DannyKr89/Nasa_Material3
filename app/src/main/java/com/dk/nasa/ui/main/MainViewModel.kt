package com.dk.nasa.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dk.nasa.BuildConfig
import com.dk.nasa.model.RepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<>,
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun sendRequest(){
        repositoryImpl.getPictureOfTheDayApi().getPictureOfTheDay(BuildConfig.NASA_API_KEY)
    }
}