package com.example.cs134onimaidsfinal.Repository

import com.example.cs134onimaidsfinal.Server.ApiServices

class WeatherRepository(val api:ApiServices) {

    fun getCurrentWeather(lat: Double,lng:Double,unit:String)=
        api.getCurrentWeather(lat,lng,unit,"3186f6ad03694127d4157192c03d3960")
}