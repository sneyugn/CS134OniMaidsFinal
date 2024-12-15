package com.example.cs134onimaidsfinal.Activity


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import java.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cs134onimaidsfinal.R
import com.example.cs134onimaidsfinal.ViewModel.WeatherViewModel
import com.example.cs134onimaidsfinal.databinding.ActivityMainBinding
import com.example.cs134onimaidsfinal.Adapter.ForecastAdapter
import com.example.cs134onimaidsfinal.model.ForecastResponseApi
import com.example.cs134onimaidsfinal.model.CurrentResponseApi
import retrofit2.Call
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel:WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE)


        binding.apply {
            var lat = intent.getDoubleExtra("lat", 0.0)
            var lon = intent.getDoubleExtra("lon", 0.0)
            var name = intent.getStringExtra("name")


            if (lat == 0.0) {
                lat = 33.1959
                lon = -117.3795
                name = "Oceanside"
           }

            // Save city name to SharedPreferences when the Add City button is clicked
            addCity.setOnClickListener {
                saveSearch(name)
                startActivity(Intent(this@MainActivity, CityListActivity::class.java))
            }

            cityTxt.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "imperial")
                .enqueue(object : retrofit2.Callback<CurrentResponseApi> {
                    override fun onResponse(
                        call: Call<CurrentResponseApi>,
                        response: Response<CurrentResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            binding.progressBar.visibility = View.GONE
                            binding.detailLayout.visibility = View.VISIBLE

                            data?.let {
                                binding.statusTxt.text = it.weather?.get(0)?.main ?: "-"
                                binding.windTxt.text = it.wind?.speed?.let { Math.round(it).toString() } + " mph"
                                binding.humidityTxt.text = it.main?.humidity?.toString() + "%"
                                binding.currentTempTxt.text = it.main?.temp?.let { Math.round(it).toString() } + "°"
                                binding.maxTempTxt.text = it.main?.tempMax?.let { Math.round(it).toString() } + "°"
                                binding.minTempTxt.text = it.main?.tempMin?.let { Math.round(it).toString() } + "°"

//                                val drawable = if (isNightNow()) R.drawable.nightbg
//                                else {
                                    val drawable = setDynamicallyWallpaper(it.weather?.get(0)?.icon ?: "-")
//                                }
                                binding.bgImage.setImageResource(drawable)
                            }
                        }
                    }

                    override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                        Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                    }
                })

//forecast temp
            weatherViewModel.loadForecastWeather(lat, lon, "imperial")
                .enqueue(object : retrofit2.Callback<ForecastResponseApi> {
                    override fun onResponse(
                        call: Call<ForecastResponseApi>,
                        response: Response<ForecastResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            blueView.visibility = View.VISIBLE

                            data?.let {
                                forecastAdapter.differ.submitList(it.list)
                                forecastView.apply {
                                    layoutManager = LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = forecastAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {

                    }

                })



        }
    }

    // Save the search to SharedPreferences
    private fun saveSearch(cityName: String?) {
        if (cityName == null || cityName.isEmpty()) return

        val editor = sharedPreferences.edit()
        val savedSearches = sharedPreferences.getStringSet("SearchList", mutableSetOf()) ?: mutableSetOf()

        savedSearches.add(cityName) // Add the new search
        editor.putStringSet("SearchList", savedSearches)
        editor.apply()
        Toast.makeText(this, "$cityName saved to search history", Toast.LENGTH_SHORT).show()
    }


        private fun isNightNow(): Boolean {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            return hour >= 18 || hour < 6
        }

        private fun setDynamicallyWallpaper(icon: String): Int {
            return when (icon.dropLast(1)) {
                "01" -> R.drawable.nightbg
                "02", "03", "04" -> R.drawable.cloudybg
                "09", "10", "11" -> R.drawable.rainbg
                "13" -> R.drawable.snowybg
                "50" -> R.drawable.hazebg
                else -> 0
            }
        }

    }

