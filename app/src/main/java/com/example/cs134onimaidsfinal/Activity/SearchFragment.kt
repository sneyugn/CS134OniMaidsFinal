package com.example.cs134onimaidsfinal.Activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cs134onimaidsfinal.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchEditText: EditText = view.findViewById(R.id.searchEditText)
        val searchButton: Button = view.findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val cityName = searchEditText.text.toString()
            if (cityName.isNotEmpty()) {
                searchCity(cityName)
            } else {
                Toast.makeText(activity, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        //        return view
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    private fun searchCity(cityName: String) {
        Toast.makeText(activity, "Searching for $cityName", Toast.LENGTH_SHORT).show()
        // Implement the logic to search for the city
    }

}