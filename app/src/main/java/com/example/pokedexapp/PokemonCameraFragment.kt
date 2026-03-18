package com.example.pokedexapp

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.theme.PokedexAppTheme

class PokemonCameraFragment : Fragment() {
    private val pokemonViewModel: PokemonViewModel by activityViewModels()

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
                            onNavigateBack = {
                                findNavController().popBackStack()
                            },
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