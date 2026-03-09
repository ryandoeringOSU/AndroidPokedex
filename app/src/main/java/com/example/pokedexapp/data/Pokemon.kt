package com.example.pokedexapp.data

import com.squareup.moshi.Json

data class Pokemon(
    val name: String,
    val species: PokemonSpeciesReference,
    val sprites: PokemonSprites,
    val types: List<PokemonTypeSlot>,
    val abilities: List<PokemonAbilitySlot>
)

data class PokemonSpeciesReference(
    val name: String,
    val url: String
)

data class PokemonSprites(
    @Json(name = "front_default") val frontDefault: String?,
    val other: PokemonOtherSprites?
)

data class PokemonOtherSprites(
    @Json(name = "official-artwork") val officialArtwork: PokemonOfficialArtwork?
)

data class PokemonOfficialArtwork(
    @Json(name = "front_default") val frontDefault: String?
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonType
)

data class PokemonType(
    val name: String,
    val url: String
)

data class PokemonAbilitySlot(
    val ability: PokemonAbility,
    @Json(name = "is_hidden") val isHidden: Boolean,
    val slot: Int
)

data class PokemonAbility(
    val name: String,
    val url: String
)
