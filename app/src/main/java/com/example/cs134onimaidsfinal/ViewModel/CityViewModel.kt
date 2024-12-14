package com.example.cs134onimaidsfinal.ViewModel

import androidx.lifecycle.ViewModel
import com.example.cs134onimaidsfinal.Repository.CityRepository
import com.example.cs134onimaidsfinal.Server.ApiClient
import com.example.cs134onimaidsfinal.Server.ApiServices
import retrofit2.create

class CityViewModel(val repository: CityRepository) : ViewModel() {
    constructor() : this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCity(q: String, limit: Int) =
        repository.getCities(q, limit)
}