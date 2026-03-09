package com.example.pokedexapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.data.LoadingStatus
import com.example.pokedexapp.data.PokeApiService
import com.example.pokedexapp.data.Pokemon
import com.example.pokedexapp.data.PokemonRepository
import com.example.pokedexapp.data.PokemonSpecies
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val repository = PokemonRepository(PokeApiService.create())

    private val _pokemon = MutableLiveData<Pokemon?>(null)
    val pokemon: LiveData<Pokemon?> = _pokemon

    private val _species = MutableLiveData<PokemonSpecies?>(null)
    val species: LiveData<PokemonSpecies?> = _species

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadPokemon(name: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            _errorMessage.value = null
            
            val pokemonResult = repository.getPokemonDetails(name)
            val speciesResult = repository.getPokemonSpecies(name)

            if (pokemonResult.isSuccess && speciesResult.isSuccess) {
                _pokemon.value = pokemonResult.getOrNull()
                _species.value = speciesResult.getOrNull()
                _loadingStatus.value = LoadingStatus.SUCCESS
            } else {
                _pokemon.value = null
                _species.value = null
                _errorMessage.value = pokemonResult.exceptionOrNull()?.message 
                    ?: speciesResult.exceptionOrNull()?.message
                _loadingStatus.value = LoadingStatus.ERROR
            }
        }
    }
}
