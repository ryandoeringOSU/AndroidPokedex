package com.example.pokedexapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pokedexapp.data.LoadingStatus
import com.example.pokedexapp.ui.PokemonViewModel
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
                    PokemonSearchRoute(
                        viewModel = sharedViewModel,
                        onOpenDetails = {
                            findNavController().navigate(R.id.action_search_to_detail)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonSearchRoute(
    viewModel: PokemonViewModel,
    onOpenDetails: () -> Unit
) {
    val pokemon by viewModel.pokemon.observeAsState()
    val loadingStatus by viewModel.loadingStatus.observeAsState(LoadingStatus.SUCCESS)
    val errorMessage by viewModel.errorMessage.observeAsState()

    var searchSubmitted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pokemon, loadingStatus, searchSubmitted) {
        if (
            searchSubmitted &&
            pokemon != null &&
            loadingStatus == LoadingStatus.SUCCESS
        ) {
            searchSubmitted = false
            onOpenDetails()
        }
    }

    PokemonSearchScreen(
        onSearch = { searchQuery ->
            if (searchQuery.isNotBlank()) {
                searchSubmitted = true
                viewModel.loadPokemon(searchQuery.trim())
            }
        },
        isLoading = loadingStatus == LoadingStatus.LOADING,
        errorMessage = errorMessage
    )
}

@Composable
private fun PokemonSearchScreen(
    onSearch: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    var searchText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Search for one of the original 151 Pokémon.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Pokémon name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSearch(searchText) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}