package com.example.tastymood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecetaDao {
    // Consulta para obtener recetas según TODOS los filtros
    @Query("""SELECT * FROM recetas 
            WHERE emocion = :emocion AND tipoDieta = :tipoDieta
            AND (ingredientes LIKE '%' || :preferredIngredients || '%' OR :preferredIngredients = '')
            AND (NOT (ingredientes LIKE '%' || :excludedIngredientes || '%') OR :excludedIngredientes = '')
        """)
    suspend fun getPrefRecetas(emocion: String, tipoDieta: String, preferredIngredients: String, excludedIngredientes: String): List<Receta>

    // Consulta para obtener recetas según los filtros MENOS dieta
    @Query("""SELECT * FROM recetas 
            WHERE emocion = :emocion
            AND (ingredientes LIKE '%' || :preferredIngredients || '%' OR :preferredIngredients = '')
            AND (NOT (ingredientes LIKE '%' || :excludedIngredientes || '%') OR :excludedIngredientes = '')
        """)
    suspend fun getAllRecetas(emocion: String, preferredIngredients: String, excludedIngredientes: String): List<Receta>

    @Query("SELECT * FROM recetas WHERE idReceta = :idReceta")
    suspend fun getRecetaById(idReceta: Int): Receta?

    @Query("""SELECT * FROM favoritos 
            WHERE idReceta = :idReceta""")
    suspend fun isFavReceta(idReceta: Int): List<Favorito>

    @Insert
    suspend fun insertReceta(receta: Receta): Long

    @Delete
    suspend fun deleteReceta(receta: Receta)
}

@Dao
interface FavoritoDao {
    @Query("""SELECT r.* FROM recetas r 
            INNER JOIN favoritos f WHERE r.idReceta = f.idReceta""")
    suspend fun getRecetasFavoritas(): List<Receta>

    @Insert
    suspend fun insertFavorito(favorito: Favorito)

    @Delete
    suspend fun deleteFavorito(favorito: Favorito)

    @Query("DELETE FROM favoritos WHERE idReceta = :idReceta")
    suspend fun deleteFavorito(idReceta: Int)
}