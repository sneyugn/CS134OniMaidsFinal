package com.example.cs134onimaidsfinal.Activity

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.cs134onimaidsfinal.model.CityResponseApi
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
        weatherApiService.getCityCoordinates(cityName, 1, "3186f6ad03694127d4157192c03d3960")
            .enqueue(object : Callback<List<CityResponseApi>> {
                override fun onResponse(call: Call<List<CityResponseApi>>, response: Response<List<CityResponseApi>>) {
                    if (response.isSuccessful) {
                        val cityData = response.body()?.firstOrNull()
                        if (cityData != null) {
//                            val lat = cityData.map.lat
//                            val lon = cityData.lon
                            //hard coded values, can delete later
                            val lat = 34.052235
                            val lon = -118.243683
                            getCurrentWeather(lat, lon, cityName)
                        } else {
                            Toast.makeText(this@AddCityActivity, "City not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AddCityActivity, "City not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<CityResponseApi>>, t: Throwable) {
                    Toast.makeText(this@AddCityActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getCurrentWeather(lat: Double, lon: Double, cityName: String) {
        val weatherApiService = ApiServices.create()
        weatherApiService.getCurrentWeather(lat, lon, "imperial", "3186f6ad03694127d4157192c03d3960")
            .enqueue(object : Callback<CurrentResponseApi> {
                override fun onResponse(call: Call<CurrentResponseApi>, response: Response<CurrentResponseApi>) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        weatherData?.let {
                            val intent = Intent()
                            intent.putExtra("cityName", cityName)
                            intent.putExtra("lat", lat)
                            intent.putExtra("lon", lon)
                            intent.putExtra("temp", it.main?.temp ?: 0.0)
                            setResult(RESULT_OK, intent)
                            finish()
                            Toast.makeText(this@AddCityActivity, "Weather: ${it.main?.temp}°F", Toast.LENGTH_SHORT).show()
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
