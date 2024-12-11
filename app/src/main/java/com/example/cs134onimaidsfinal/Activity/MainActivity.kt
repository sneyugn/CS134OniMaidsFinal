package com.example.cs134onimaidsfinal.Activity


import android.content.Intent
import android.graphics.Color
import java.util.Calendar
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.cs134onimaidsfinal.Activity.SearchFragment
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelLazy
import com.example.cs134onimaidsfinal.R
import com.example.cs134onimaidsfinal.ViewModel.WeatherViewModel
import com.example.cs134onimaidsfinal.databinding.ActivityMainBinding
import com.example.cs134onimaidsfinal.Adapter.ForecastAdapter
import com.example.cs134onimaidsfinal.model.ForecastResponseApi
import com.example.cs134onimaidsfinal.model.CurrentResponseApi
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel:WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //added here
        val addCity: Button = findViewById(R.id.addCity)
        addCity.setOnClickListener {
//            loadFragment(SearchFragment())
            var intent = Intent(this, AddCityActivity::class.java)
            startActivity(intent)
//            findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainActivity_to_addCityActivity)
        } //end here


        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = 33.20
            var lon = -117.38
            var name = "Oceanside"

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
                                binding.windTxt.text = it.wind?.speed?.let { Math.round(it).toString() } + "mph"
                                binding.humidityTxt.text = it.main?.humidity?.toString() + "%"
                                binding.currentTempTxt.text = it.main?.temp?.let { Math.round(it).toString() } + "°"
                                binding.maxTempTxt.text = it.main?.tempMax?.let { Math.round(it).toString() } + "°"
                                binding.minTempTxt.text = it.main?.tempMin?.let { Math.round(it).toString() } + "°"

                                val drawable = if (isNightNow()) R.drawable.night_bg
                                else {
                                    setDynamicallyWallpaper(it.weather?.get(0)?.icon ?: "-")
                                }
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



        private fun isNightNow(): Boolean {
            return calendar.get(Calendar.HOUR_OF_DAY) >= 18
        }

        private fun setDynamicallyWallpaper(icon: String): Int {
            return when (icon.dropLast(1)) {
                "01" -> R.drawable.night_bg
                "02", "03", "04" -> R.drawable.cloudy_bg
                "09", "10", "11" -> R.drawable.rainy_bg
                "13" -> R.drawable.snow_bg
                "50" -> R.drawable.haze_bg
                else -> 0
            }
        }

    //added here
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    } //end here

    }

