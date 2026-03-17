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
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.theme.PokedexAppTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokedexapp.data.Pokemon
import com.example.pokedexapp.data.PokemonSpecies

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

                    PokemonDetailScreen(
                        pokemon = pokemon,
                        species = species,
                        onOpenCamera = {
                            findNavController().navigate(R.id.action_detail_to_camera)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun PokemonDetailScreen(
    pokemon: Pokemon?,
    species: PokemonSpecies?,
    onOpenCamera: () -> Unit
) {

    if (pokemon == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "No Pokémon selected.",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        return
    }

    val artworkUrl =
        pokemon.sprites.other?.officialArtwork?.frontDefault
            ?: pokemon.sprites.frontDefault

    val flavorText =
        species?.flavorTextEntries
            ?.firstOrNull { it.language.name == "en" }
            ?.flavorText
            ?.replace("\n", " ")
            ?.replace("\u000c", " ")
            ?: "No description available."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = pokemon.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = artworkUrl,
            contentDescription = pokemon.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Types", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        pokemon.types.forEach {
            Text("• ${it.type.name}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Abilities", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        pokemon.abilities.forEach {
            Text("• ${it.ability.name}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Description", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text(flavorText)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onOpenCamera,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Camera")
        }
    }
}