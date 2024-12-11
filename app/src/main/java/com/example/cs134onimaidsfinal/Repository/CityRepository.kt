package com.example.cs134onimaidsfinal.Repository

import com.example.cs134onimaidsfinal.Server.ApiServices

class CityRepository(val api: ApiServices) {
    fun getCities(q: String, limit: Int) =
        api.getCitiesList(q, limit, "3186f6ad03694127d4157192c03d3960")
}