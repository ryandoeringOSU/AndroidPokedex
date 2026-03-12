package com.example.pokedexapp

/**
 * Frontend usage:
 * - Use this fragment as the drop-in camera screen from your activity or fragment navigation.
 * - It hosts the Compose UI in `PokemonCameraScreen` for the camera + sprite overlay flow.
 * - If `PokemonViewModel` has a loaded Pokemon, the camera uses that Pokemon's `sprites.frontDefault`.
 * - If nothing is loaded yet, it automatically falls back to Pikachu.
 * - If you only need the UI inside an existing Compose screen, use `PokemonCameraScreen` directly instead.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.theme.PokedexAppTheme

class PokemonCameraFragment : Fragment() {
    private val pokemonViewModel: PokemonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PokedexAppTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        PokemonCameraScreen(
                            viewModel = pokemonViewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
