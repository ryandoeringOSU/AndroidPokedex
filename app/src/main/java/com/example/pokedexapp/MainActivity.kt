package com.example.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.pokedexapp.ui.theme.PokedexAppTheme
import com.example.pokedexapp.ui.home.HomeScreen
import com.example.pokedexapp.ui.detail.DetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // CameraFeatureScreen(modifier = Modifier.padding(innerPadding))
                    /**HomeScreen(
                        onNavigateToDetail = { },
                        modifier = Modifier.padding(innerPadding)
                    ) **/

                    DetailScreen(
                        pokemonName = "pikachu",
                        onNavigateBack = { },
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

