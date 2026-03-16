package com.example.pokedexapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> ViewStateWrapper(
    uiState: LoadingStatus<T>,
    onRetry: () -> Unit,
    content: @Composable (T) -> Unit
) {
    when (uiState) {
        is LoadingStatus.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is LoadingStatus.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Error loading Pokémon. Please try again.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
        is LoadingStatus.Success -> {
            // Injects Katie's UI when the API call succeeds
            content(uiState.data)
        }
        else -> {} // Handle the idle state before a search happens
    }
}