package com.example.pokedexapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pokedexapp.ui.PokemonViewModel

class PokemonSearchFragment : Fragment() {
    // Shares the exact same ViewModel across the entire Activity
    private val sharedViewModel: PokemonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedBundle: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by sharedViewModel.pokemonState.collectAsState()

                // Listens for a successful search (or Bailey's SharedPreferences auto-load)
                LaunchedEffect(uiState) {
                    if (uiState is LoadingStatus.Success && !sharedViewModel.hasNavigated) {
                        findNavController().navigate(R.id.action_search_to_detail)
                        sharedViewModel.markAsNavigated() // Prevents infinite navigation loops
                    }
                }

                // Katie's Search UI
                PokemonSearchScreen(
                    onSearch = { searchQuery ->
                        sharedViewModel.hasNavigated = false
                        sharedViewModel.fetchPokemon(searchQuery)
                    }
                )
            }
        }
    }
}