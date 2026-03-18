package com.example.pokedexapp.data

import com.squareup.moshi.Json

data class Pokemon(
    val name: String,
    val sprites: PokemonSprites,
    val types: List<PokemonTypeSlot>,
    val abilities: List<PokemonAbilitySlot>
)


data class PokemonSprites(
    @param:Json(name = "front_default") val frontDefault: String?,
    val other: PokemonOtherSprites?
)

data class PokemonOtherSprites(
    @param:Json(name = "official-artwork") val officialArtwork: PokemonOfficialArtwork?
)

data class PokemonOfficialArtwork(
    @param:Json(name = "front_default") val frontDefault: String?
)

data class PokemonTypeSlot(
    val type: PokemonType
)

data class PokemonType(
    val name: String,
)

data class PokemonAbilitySlot(
    val ability: PokemonAbility,
)

data class PokemonAbility(
    val name: String,
)
