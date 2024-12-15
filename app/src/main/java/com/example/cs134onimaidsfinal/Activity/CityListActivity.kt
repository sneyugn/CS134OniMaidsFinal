package com.example.cs134onimaidsfinal.Activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs134onimaidsfinal.Adapter.CityAdapter
import com.example.cs134onimaidsfinal.R
import com.example.cs134onimaidsfinal.ViewModel.CityViewModel
import com.example.cs134onimaidsfinal.databinding.ActivityCityListBinding
import com.example.cs134onimaidsfinal.databinding.CityViewholderBinding
import com.example.cs134onimaidsfinal.model.CityResponseApi
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import android.widget.Toast
import com.example.cs134onimaidsfinal.Adapter.SavedSearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCityListBinding
    private val cityAdapter by lazy { CityAdapter() }
    private val cityViewModel: CityViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE)

        // Load and display saved searches
//        displaySavedSearches()
//        loadSavedSearches()
        binding.apply {
            cityEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    progressBar2.visibility = View.VISIBLE
                    cityViewModel.loadCity(s.toString(), 10)
                        .enqueue(object : Callback<CityResponseApi> {
                            override fun onResponse(
                                call: Call<CityResponseApi>,
                                response: Response<CityResponseApi>
                            ) {
                                if (response.isSuccessful) {
                                    val data = response.body()
                                    data?.let {
                                        cityAdapter.differ.submitList(it)
                                        progressBar2.visibility = View.GONE
                                        cityAdapter.differ.submitList(it)
                                        cityView.apply {
                                            layoutManager = LinearLayoutManager(
                                                this@CityListActivity,
                                                LinearLayoutManager.HORIZONTAL,
                                                false
                                            )
                                            adapter = cityAdapter
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<CityResponseApi>, t: Throwable) {

                            }

                        })
                }

            })


            // Save button functionality
            saveButton.setOnClickListener {
                val cityName = cityEdt.text.toString()
                if (cityName.isNotEmpty()) {
//                    saveSearch(cityName)
//                    loadSavedSearches()
                    cityEdt.text.clear()
                    Toast.makeText(this@CityListActivity, "$cityName saved!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CityListActivity, "Please enter a city name", Toast.LENGTH_SHORT).show()
                }
            }

            //back button
            val backButton: Button = findViewById(R.id.backButton)
            backButton.setOnClickListener {
                val intent = Intent(this@CityListActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    // Save search to SharedPreferences
//    private fun saveSearch(cityName: String) {
//        val editor = sharedPreferences.edit()
//        val savedSearches = sharedPreferences.getStringSet("SearchList", mutableSetOf()) ?: mutableSetOf()
//        savedSearches.add(cityName)
//        editor.putStringSet("SearchList", savedSearches)
//        editor.apply()
//    }

    // Load saved searches and update RecyclerView
//    private fun loadSavedSearches() {
//        val savedSearches = sharedPreferences.getStringSet("SearchList", mutableSetOf())?.toList() ?: emptyList()
//        val cityResponseItems = savedSearches.map { cityName ->
//            CityResponseApi.CityResponseApiItem(
//                name = cityName,
//                lat = getLatitudeForCity(cityName),
//                lon = getLongitudeForCity(cityName),
//                state = null, // or provide a default value if available
//                country = null, // or provide a default value if available
//                localNames = null // or provide a default value if available
//            )
//        }
//        cityAdapter.differ.submitList(cityResponseItems)
//    }

//private fun getLatitudeForCity(cityName: String): Double {
//        val cityCoordinates = mapOf(
//            "New York" to 40.7128,
//            "Los Angeles" to 34.0522,
//            "Chicago" to 41.8781,
//            "Houston" to 29.7604,
//            "Phoenix" to 33.4484,
//            "Philadelphia" to 39.9526,
//            "San Antonio" to 29.4241,
//            "San Diego" to 32.7157,
//            "Dallas" to 32.7767,
//            "San Jose" to 37.3382,
//            "Austin" to 30.2672,
//            "Jacksonville" to 30.3322,
//            "Fort Worth" to 32.7555,
//            "Columbus" to 39.9612,
//            "Charlotte" to 35.2271,
//            "San Francisco" to 37.7749,
//            "Indianapolis" to 39.7684,
//            "Seattle" to 47.6062,
//            "Denver" to 39.7392,
//            "Washington" to 38.9072
//        )
//        return cityCoordinates[cityName] ?: 0.0
//    }

//    private fun getLongitudeForCity(cityName: String): Double {
//        val cityCoordinates = mapOf(
//            "New York" to -74.0060,
//            "Los Angeles" to -118.2437,
//            "Chicago" to -87.6298,
//            "Houston" to -95.3698,
//            "Phoenix" to -112.0740,
//            "Philadelphia" to -75.1652,
//            "San Antonio" to -98.4936,
//            "San Diego" to -117.1611,
//            "Dallas" to -96.7970,
//            "San Jose" to -121.8863,
//            "Austin" to -97.7431,
//            "Jacksonville" to -81.6557,
//            "Fort Worth" to -97.3308,
//            "Columbus" to -82.9988,
//            "Charlotte" to -80.8431,
//            "San Francisco" to -122.4194,
//            "Indianapolis" to -86.1581,
//            "Seattle" to -122.3321,
//            "Denver" to -104.9903,
//            "Washington" to -77.0369
//        )
//        return cityCoordinates[cityName] ?: 0.0
//    }


}