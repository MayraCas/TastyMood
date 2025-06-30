package com.example.tastymood.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "recetas")
data class Receta(
    @PrimaryKey(autoGenerate = true) val idReceta: Int = 0,
    val nombreReceta: String,
    val ingredientes: String,
    val preparacion: String,
    val imagen: String, // URL o path de la imagen
    val tipoDieta: String,
    val emocion: String
)

@Entity(
    tableName = "favoritos",
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["idReceta"],
            childColumns = ["idReceta"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idReceta"])]
)
data class Favorito(
    @PrimaryKey(autoGenerate = true) val idFav: Int = 0,
    val idReceta: Int
)
