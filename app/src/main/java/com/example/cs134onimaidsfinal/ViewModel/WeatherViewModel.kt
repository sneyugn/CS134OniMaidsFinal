package com.example.cs134onimaidsfinal.ViewModel

import androidx.lifecycle.ViewModel
import com.example.cs134onimaidsfinal.Repository.WeatherRepository
import com.example.cs134onimaidsfinal.Server.ApiClient
import com.example.cs134onimaidsfinal.Server.ApiServices

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat: Double, lng: Double, unit: String) =
        repository.getCurrentWeather(lat, lng, unit)

    fun loadForecastWeather(lat: Double, lng: Double, unit: String) =
        repository.getForecastWeather(lat, lng, unit)
}