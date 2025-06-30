package com.example.tastymood

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tastymood.database.Receta
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tastymood.database.AppDatabase
import com.example.tastymood.database.Favorito
import kotlinx.coroutines.launch

@Composable
fun RecetasDetalle(
    navController: NavHostController,
    idReceta: Int
) {
    val recetaViewModel: RecetaViewModel = viewModel()
    val filtros = recetaViewModel.filtros
    val database = recetaViewModel.database

    var receta by remember { mutableStateOf<Receta?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(idReceta) { // Usa idReceta como clave de efecto
        isLoading = true
        errorMessage = null
        try {
            receta = database.recetaDao().getRecetaById(idReceta)
            Log.d("Recetas Favoritas", "Receta cargada: $receta") // Verifica si está obteniendo datos
        } catch (e: Exception) {
            Log.e("Recetas Favoritas", "Error al cargar la receta", e)
            errorMessage = "Error al cargar la receta"
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFC6BB)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 30.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = Color(0xFFAC5969),
                        modifier = Modifier.padding(20.dp)
                    )
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }

                receta != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                             AsyncImage(
                                 model = receta!!.imagen,
                                 contentDescription = receta!!.nombreReceta,
                                 modifier = Modifier.fillMaxSize()
                                     .padding(10.dp)
                                     .clip(RoundedCornerShape(30.dp)),
                                 contentScale = ContentScale.Crop,
                                 alignment = Alignment.Center
                             )

                            // Botón de regreso
                            FloatingActionButton(
                                onClick = {
                                    navController.popBackStack()
                                },
                                contentColor = Color(0xFFAC5969),
                                containerColor = Color(0xFFFFEBEB),
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(15.dp),
                                shape = CircleShape,
                                elevation = FloatingActionButtonDefaults.elevation(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    modifier = Modifier.size(30.dp),
                                    contentDescription = "Regresar"
                                )
                            }
                        }
                        cardDatosReceta(receta!!, database)
                    }
                }
                else -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFC6BB)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "😔",
                                fontSize = 40.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "No se pudieron cargar los detalles de la receta",
                                color = Color(0xFFAC5969),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Por favor vuelva a intentarlo más tarde",
                                color = Color(0xFFC07771),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun cardDatosReceta(
    receta: Receta,
    database: AppDatabase
) {

    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()

    var fav by remember { mutableStateOf<List<Favorito>>(emptyList()) }
    var isFavorite by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(receta) {
        isLoading = true
        errorMessage = null
        try {
            fav = database.recetaDao().isFavReceta(idReceta = receta.idReceta)
            isFavorite = fav.isNotEmpty()
        } catch (e: Exception) {
            Log.e("Favoritos", "Error al verificar recetas favoritas", e)
            errorMessage = "Error al verificar favoritas"
        } finally {
            isLoading = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEB)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            // Título con estrella
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = receta.nombreReceta,
                    fontSize = 24.sp,
                    color = Color(0xFFAC5969),
                    modifier = Modifier.weight(1f)
                )

                // Botón de favoritos
                IconButton(
                    onClick = {
                        scope.launch {
                            if (isFavorite) {
                                database.favoritoDao().deleteFavorito(receta.idReceta)
                            } else {
                                val favorito = Favorito(idReceta = receta.idReceta)
                                database.favoritoDao().insertFavorito(favorito)
                            }
                            isFavorite = !isFavorite
                        }
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFFAD5D56), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Agregar a favoritos",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ingredientes
            Text(
                text = "Ingredientes:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFAC5969)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFC6BB)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {

                val ingredientesList =
                    receta.ingredientes.split(".").map { it.trim() }.filter { it.isNotBlank() }
                Text(
                    buildAnnotatedString {
                        ingredientesList.forEachIndexed() { index, ingrediente ->
                            append("•  ")
                            append(ingrediente)
                            if (index != ingredientesList.lastIndex) {
                                append(".\n")
                            } else{
                                append(".")
                            }
                        }
                    },
                    modifier = Modifier.padding(20.dp),
                    color = Color(0xFFAC5969),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Preparación
            Text(
                text = "Preparación:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFAC5969)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFC6BB)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {

                val preparacionList =
                    receta.preparacion.split(".").map { it.trim() }.filter { it.isNotBlank() }
                Text(
                    buildAnnotatedString {
                        preparacionList.forEachIndexed { index, preparacion ->
                            append("${index + 1}. $preparacion")
                            if (index != preparacionList.lastIndex) {
                                append(".\n")
                            } else{
                                append(".")
                            }
                        }
                    },
                    modifier = Modifier.padding(20.dp),
                    color = Color(0xFFAC5969),
                    fontSize = 14.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}