package com.example.cs134onimaidsfinal.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cs134onimaidsfinal.R

class SearchCityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_city)

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val searchButton: Button = findViewById(R.id.searchButton)
        val addCityButton: Button = findViewById(R.id.addCityButton)

        searchButton.setOnClickListener {
            val cityName = searchEditText.text.toString()
            if (cityName.isNotEmpty()) {
                searchCity(cityName)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        addCityButton.setOnClickListener {
            //added SearchFragment to the fragment container
            loadFragment(SearchFragment())
        }
    }

    private fun searchCity(cityName: String) {
        Toast.makeText(this, "Searching for $cityName", Toast.LENGTH_SHORT).show()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}