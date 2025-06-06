package com.example.tastymood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecetaDao {
    @Query("""SELECT * FROM recetas 
            WHERE emocion = :emocion AND tipoDieta = :tipoDieta
            AND (ingredientes LIKE '%' || :preferredIngredients || '%')
            AND NOT (ingredientes LIKE '%' || :excludedIngredientes || '%')
        """)
    suspend fun getAllRecetas(emocion: String, tipoDieta: String, preferredIngredients: String, excludedIngredientes: String): List<Receta>

    @Query("SELECT * FROM recetas WHERE tipoDieta = :tipoDieta")
    suspend fun getRecetasByDieta(tipoDieta: String): List<Receta>

    @Query("SELECT * FROM recetas WHERE emocion = :emocion")
    suspend fun getRecetasByEmocion(emocion: String): List<Receta>

    @Insert
    suspend fun insertReceta(receta: Receta): Long

    @Delete
    suspend fun deleteReceta(receta: Receta)
}

@Dao
interface FavoritoDao {
    @Query("""SELECT r.* FROM recetas r 
            INNER JOIN favoritos f ON r.idReceta = f.idReceta""")
    suspend fun getRecetasFavoritas(): List<Receta>

    @Insert
    suspend fun insertFavorito(favorito: Favorito)

    @Delete
    suspend fun deleteFavorito(favorito: Favorito)

    @Query("DELETE FROM favoritos WHERE idReceta = :idReceta")
    suspend fun deleteFavorito(idReceta: Int)
}