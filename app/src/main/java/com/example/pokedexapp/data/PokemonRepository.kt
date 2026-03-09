package com.example.pokedexapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(
    private val service: PokeApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getPokemonDetails(name: String): Result<Pokemon> =
        withContext(ioDispatcher) {
            try {
                val response = service.getPokemonDetails(name.lowercase())
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun getPokemonSpecies(name: String): Result<PokemonSpecies> =
        withContext(ioDispatcher) {
            try {
                val response = service.getPokemonSpecies(name.lowercase())
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
