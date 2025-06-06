package com.example.tastymood

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tastymood.utils.DataStoreManager

@Composable
fun Navegacion(dataStoreManager: DataStoreManager) {
    val navController = rememberNavController()
    NavHost(navController = navController, "Home") {
        composable(route = "Home") {
            PantallaHome(navController, dataStoreManager)
        }
        composable(route = "Recetas/{mood}/{diets}/{selectedIngredients}/{excludedIngredients}") { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood") ?: "Feliz"
            val diets = backStackEntry.arguments?.getString("diets") ?: "Ninguna"
            val selectIng = backStackEntry.arguments?.getString("selectedIngredients") ?: "Ninguno"
            val excludedIng = backStackEntry.arguments?.getString("excludedIngredients") ?: "Ninguno"

            Recetas(navController, mood, diets, selectIng, excludedIng)
        }

    }
}