package com.example.pokedexapp.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferencesRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("pokedex_prefs", Context.MODE_PRIVATE)

    fun saveLastSearch(name: String) {
        sharedPreferences.edit().putString("last_search", name).apply()
    }

    fun getLastSearch(): String? {
        return sharedPreferences.getString("last_search", null)
    }
}
