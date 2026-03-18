package com.example.pokedexapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pokedexapp.data.LoadingStatus
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.home.HomeScreen
import com.example.pokedexapp.ui.theme.PokedexAppTheme

class PokemonSearchFragment : Fragment() {
    private val sharedViewModel: PokemonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PokedexAppTheme {
                    val pokemon by sharedViewModel.pokemon.observeAsState()
                    val loadingStatus by sharedViewModel.loadingStatus.observeAsState(LoadingStatus.SUCCESS)
                    val errorMessage by sharedViewModel.errorMessage.observeAsState()
                    val navigateToDetail by sharedViewModel.navigateToDetail.observeAsState(false)

                    var searchText by rememberSaveable { mutableStateOf("") }

                    LaunchedEffect(pokemon?.name) {
                        val pokemonName = pokemon?.name
                        if (searchText.isBlank() && !pokemonName.isNullOrBlank()) {
                            searchText = pokemonName
                        }
                    }

                    LaunchedEffect(navigateToDetail) {
                        if (navigateToDetail) {
                            findNavController().navigate(R.id.action_search_to_detail)
                            sharedViewModel.onDetailNavigationHandled()
                        }
                    }

                    HomeScreen(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        onSearchClick = {
                            val trimmed = searchText.trim()
                            if (trimmed.isNotEmpty()) {
                                sharedViewModel.loadPokemon(trimmed)
                            }
                        },
                        isLoading = loadingStatus == LoadingStatus.LOADING,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }
}