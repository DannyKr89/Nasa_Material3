package com.dk.nasa.ui.main

import com.dk.nasa.model.PictureOfTheDayData

sealed class AppState {
    data class Success(val pictureOfTheDayData: PictureOfTheDayData) : AppState()
    data class Error(val message: String) : AppState()
    object Loading : AppState()
}