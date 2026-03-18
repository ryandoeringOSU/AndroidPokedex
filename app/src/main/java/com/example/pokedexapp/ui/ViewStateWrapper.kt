package com.example.pokedexapp.ui

import androidx.compose.runtime.Composable

@Composable
fun ViewStateWrapper(
    content: @Composable () -> Unit
) {
    content()
}