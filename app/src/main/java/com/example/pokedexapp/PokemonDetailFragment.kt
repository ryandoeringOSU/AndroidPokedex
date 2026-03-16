package com.example.pokedexapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.ViewStateWrapper

class PokemonDetailFragment : Fragment() {
    // Retrieves the already-loaded Pokémon data from the shared ViewModel
    private val sharedViewModel: PokemonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedBundle: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by sharedViewModel.pokemonState.collectAsState()

                ViewStateWrapper(
                    uiState = uiState,
                    onRetry = { /* Optional retry logic */ }
                ) { pokemonData ->
                    // Katie's Detail UI
                    PokemonDetailScreen(
                        pokemon = pokemonData,
                        onOpenCameraClick = {
                            // Navigates directly to Ryan's camera feature
                            findNavController().navigate(R.id.action_detail_to_camera)
                        }
                    )
                }
            }
        }
    }
}