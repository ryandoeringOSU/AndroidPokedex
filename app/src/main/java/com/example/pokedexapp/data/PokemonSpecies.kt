package com.example.pokedexapp.data

import com.squareup.moshi.Json

data class PokemonSpecies(
    @param:Json(name = "flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    @param:Json(name = "flavor_text") val flavorText: String,
    val language: LanguageReference
)

data class LanguageReference(
    val name: String
)
