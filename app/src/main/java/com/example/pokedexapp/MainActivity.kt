package com.example.pokedexapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

// Use AppCompatActivity for better compatibility with Navigation and UI components
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // This connects to the activity_main.xml layout that contains your NavHost
        setContentView(R.layout.activity_main)

    }
}