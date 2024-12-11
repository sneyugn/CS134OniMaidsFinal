package com.example.cs134onimaidsfinal.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.cs134onimaidsfinal.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs134onimaidsfinal.model.CurrentResponseApi
import com.example.cs134onimaidsfinal.Server.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCityActivity : AppCompatActivity() {
    private lateinit var cityAdapter: CityAdapter
    private val cityList = mutableListOf<String>()
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
//            view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, insets.systemGestureInsets.bottom)
//            insets
//        }
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val searchButton: Button = findViewById(R.id.searchButton)
        val addCityButton: Button = findViewById(R.id.addCityButton)
        val backButton: Button = findViewById(R.id.backButton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        cityAdapter = CityAdapter(cityList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cityAdapter

        searchButton.setOnClickListener {
            val cityName = searchEditText.text.toString()
            if (cityName.isNotEmpty()) {
                searchCityWeather(cityName)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        addCityButton.setOnClickListener {
            val cityName = searchEditText.text.toString()
            if (cityName.isNotEmpty()) {
                addCity(cityName)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        //back button
        backButton.setOnClickListener {
            finish()
        }

    }




    private fun searchCityWeather(cityName: String) {
        val weatherApiService = ApiServices.create()
        val lon = 0.0
        val lat = 0.0
        weatherApiService.getCurrentWeather(lat, lon, "imperial", "3186f6ad03694127d4157192c03d3960")
            .enqueue(object : Callback<CurrentResponseApi> {
                override fun onResponse(call: Call<CurrentResponseApi>, response: Response<CurrentResponseApi>) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        weatherData?.let {
                            Toast.makeText(this@AddCityActivity, "Weather: ${it.main?.temp}°C", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AddCityActivity, "City not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@AddCityActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

        private fun addCity(cityName: String) {
            cityList.add(cityName)
            cityAdapter.notifyDataSetChanged()
            Toast.makeText(this, "$cityName added", Toast.LENGTH_SHORT).show()
        }

    }
