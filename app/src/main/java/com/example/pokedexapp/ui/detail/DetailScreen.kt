package com.example.pokedexapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokedexapp.data.LoadingStatus
import com.example.pokedexapp.ui.PokemonViewModel
import com.example.pokedexapp.ui.home.PokeRed
import com.example.pokedexapp.ui.home.PokeWhite

val CardGold = Color(0xFFE8C84A)
val CardDarkGold = Color(0xFFB8972A)
val CardYellow = Color(0xFFF5E97A)
val CardCream = Color(0xFFFFFDE7)
val CardBrown = Color(0xFF5A3E00)

fun typeColor(typeName: String): Color {
    return when (typeName.lowercase()) {
        "fire" -> Color(0xFFFF6B35)
        "water" -> Color(0xFF4A90D9)
        "grass" -> Color(0xFF5DBE62)
        "electric" -> Color(0xFFF7D02C)
        "psychic" -> Color(0xFFF95587)
        "ice" -> Color(0xFF96D9D6)
        "dragon" -> Color(0xFF6F35FC)
        "dark" -> Color(0xFF705746)
        "fairy" -> Color(0xFFD685AD)
        "fighting" -> Color(0xFFC22E28)
        "flying" -> Color(0xFFA98FF3)
        "poison" -> Color(0xFFA33EA1)
        "ground" -> Color(0xFFE2BF65)
        "rock" -> Color(0xFFB6A136)
        "bug" -> Color(0xFFA6B91A)
        "ghost" -> Color(0xFF735797)
        "steel" -> Color(0xFFB7B7CE)
        "normal" -> Color(0xFFA8A878)
        else -> CardDarkGold
    }
}

@Composable
fun DetailScreen(
    pokemonName: String,
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
    modifier: Modifier = Modifier,
    pokemonViewModel: PokemonViewModel = viewModel()
) {
    val pokemon by pokemonViewModel.pokemon.observeAsState()
    val species by pokemonViewModel.species.observeAsState()
    val loadingStatus by pokemonViewModel.loadingStatus.observeAsState()
    val errorMessage by pokemonViewModel.errorMessage.observeAsState()

    LaunchedEffect(pokemonName) {
        pokemonViewModel.loadPokemon(pokemonName)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2A1A00))
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(4.dp, CardDarkGold, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(CardGold)
        ) {
            when (loadingStatus ?: LoadingStatus.LOADING) {
                LoadingStatus.LOADING -> {
                    CircularProgressIndicator(
                        color = PokeRed,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                LoadingStatus.ERROR -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage ?: "Something went wrong.",
                            color = PokeRed
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateBack,
                            colors = ButtonDefaults.buttonColors(containerColor = PokeRed)
                        ) {
                            Text("Go Back", color = PokeWhite)
                        }
                    }
                }

                LoadingStatus.SUCCESS -> {
                    pokemon?.let { p ->
                        val spriteUrl = p.sprites.other?.officialArtwork?.frontDefault
                            ?: p.sprites.frontDefault ?: ""

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(12.dp)
                                .padding(bottom = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // top bar — name + HP
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = p.name.replaceFirstChar { it.uppercase() },
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CardBrown
                                )
                                Button(
                                    onClick = onNavigateBack,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CardDarkGold,
                                        contentColor = PokeWhite
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text("Back", fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // sprite area
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(3.dp, CardDarkGold, RoundedCornerShape(12.dp))
                                    .background(Color(0xFF88BB88)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = spriteUrl,
                                    contentDescription = "${p.name} sprite",
                                    modifier = Modifier.size(180.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // type badges
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                p.types.forEach { typeSlot ->
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                typeColor(typeSlot.type.name),
                                                RoundedCornerShape(20.dp)
                                            )
                                            .padding(horizontal = 16.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = typeSlot.type.name.replaceFirstChar { it.uppercase() },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PokeWhite
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // flavor text box
                            species?.let { s ->
                                val entry = s.flavorTextEntries
                                    .firstOrNull { it.language.name == "en" }
                                entry?.let {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(1.dp, CardDarkGold, RoundedCornerShape(10.dp))
                                            .background(CardCream)
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = it.flavorText.replace("\n", " "),
                                            fontSize = 16.sp,
                                            color = Color(0xFF444444),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // abilities box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, CardDarkGold, RoundedCornerShape(10.dp))
                                    .background(CardCream)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Abilities",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CardBrown
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = p.abilities.joinToString(" · ") {
                                            it.ability.name.replaceFirstChar { c -> c.uppercase() }
                                        },
                                        fontSize = 15.sp,
                                        color = Color(0xFF444444)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // bottom gold bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(CardDarkGold)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Pokédex",
                                    fontSize = 12.sp,
                                    color = PokeWhite,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                    }
                }
            }

            // camera FAB
            FloatingActionButton(
                onClick = onNavigateToCamera,
                containerColor = PokeRed,
                contentColor = PokeWhite,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Take photo"
                )
            }
        }
    }
}