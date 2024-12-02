package com.example.cs134onimaidsfinal.Activity


import android.content.Intent
import android.graphics.Color
import java.util.Calendar
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
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
import com.example.cs134onimaidsfinal.model.CurrentResponseApi
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel:WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = 33.22
            var lon = -117.35
            var name = "California"

            cityTxt.text = name
            progressBar.visibility = View.VISIBLE


            weatherViewModel.loadCurrentWeather(lat, lon, "metric")
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
                                binding.windTxt.text = it.wind?.speed?.let { Math.round(it).toString() } + "Km"
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
    }

