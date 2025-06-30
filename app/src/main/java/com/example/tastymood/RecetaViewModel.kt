package com.example.tastymood

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tastymood.database.AppDatabase

class RecetaViewModel(application: Application) : AndroidViewModel(application) {
    val database: AppDatabase = AppDatabase.getDatabase(application)

    var filtros: FiltrosReceta? = null
}

data class FiltrosReceta(
    val mood: String,
    val diet: String,
    val preferredIngredients: String,
    val excludedIngredients: String
)