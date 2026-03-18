package com.example.pokedexapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pokedexapp.data.LoadingStatus
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.detail.DetailScreen
import com.example.pokedexapp.ui.theme.PokedexAppTheme

class PokemonDetailFragment : Fragment() {
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
                    val species by sharedViewModel.species.observeAsState()
                    val loadingStatus by sharedViewModel.loadingStatus.observeAsState(LoadingStatus.LOADING)
                    val errorMessage by sharedViewModel.errorMessage.observeAsState()

                    DetailScreen(
                        pokemon = pokemon,
                        species = species,
                        loadingStatus = loadingStatus,
                        errorMessage = errorMessage,
                        onNavigateBack = {
                            findNavController().popBackStack()
                        },
                        onNavigateToCamera = {
                            findNavController().navigate(R.id.action_detail_to_camera)
                        }
                    )
                }
            }
        }
    }
}