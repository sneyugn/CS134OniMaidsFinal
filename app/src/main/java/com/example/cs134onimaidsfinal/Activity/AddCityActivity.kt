package com.example.cs134onimaidsfinal.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.cs134onimaidsfinal.R

class AddCityActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
//            view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, insets.systemGestureInsets.bottom)
//            insets
//        }
    }
}