package com.example.tastymood

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tastymood.database.Receta
import com.example.tastymood.database.Favorito
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.util.Log
import androidx.compose.runtime.*
import com.example.tastymood.database.AppDatabase
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import kotlinx.coroutines.launch
import androidx.compose.material3.IconButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.tastymood.database.FavoritoDao
import com.example.tastymood.database.RecetaDao


@Composable
fun Recetas(
    navController: NavHostController,
    mood: String,
    diet: String,
    preferredIngredients: String,
    excludedIngredients: String
){

    val recetaViewModel: RecetaViewModel = viewModel()
    recetaViewModel.filtros = FiltrosReceta(mood, diet, preferredIngredients, excludedIngredients)
    val database = recetaViewModel.database

    var recetas by remember { mutableStateOf<List<Receta>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cargar recetas
    LaunchedEffect (mood, diet, preferredIngredients, excludedIngredients) {
        isLoading = true
        errorMessage = null
        try {
            if (diet == "Ninguna") {
                recetas = database.recetaDao().getAllRecetas(
                    emocion = mood,
                    preferredIngredients = preferredIngredients,
                    excludedIngredientes = excludedIngredients
                )
            } else {
                recetas = database.recetaDao().getPrefRecetas(
                    emocion = mood,
                    tipoDieta = diet,
                    preferredIngredients = preferredIngredients,
                    excludedIngredientes = excludedIngredients
                )
            }
        } catch (e: Exception) {
            Log.e("Recetas", "Error al cargar recetas", e)
            errorMessage = "Error al cargar las recetas"
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEB)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("Home")
                    },
                    contentColor = Color(0xFFAC5969),
                    containerColor = Color(0xFFFDC7BD),
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

                Text(
                    text = "Recetas",
                    color = Color(0xFFAC5969),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(15.dp)
                        .height(60.dp)
                        .width(65.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBEB))
                        .border(2.dp, Color(0xFFAC5969), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = imagenEmogi(mood),
                        contentDescription = "Estado de ánimo",
                        modifier = Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(60.dp))
                    )
                }
            }

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

                recetas.isEmpty() -> {
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
                                text = "No se encontraron recetas",
                                color = Color(0xFFAC5969),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Intenta con otros filtros",
                                color = Color(0xFFC07771),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(recetas) { receta ->
                            RecetaCard(
                                navController = navController,
                                database = database,
                                receta = receta
                            )
                        }

                        // Espacio adicional al final
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecetaCard(
    navController: NavHostController,
    database: AppDatabase,
    receta: Receta,
) {
    val scope = rememberCoroutineScope()

    var fav by remember { mutableStateOf<List<Favorito>>(emptyList()) }
    var isFavorite by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Verificar si está la receta en favoritos
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
            .height(130.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFDC7BD)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Imagen de la receta
            val painter = rememberAsyncImagePainter(
                model = receta.imagen,
                error = painterResource(R.drawable.sincargar)
            )

            val state = painter.state

            Box(
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (state is AsyncImagePainter.State.Loading)
                            Color(0xFFFDC7BD)
                        else
                            Color.Transparent
                    )
            ) {
                Image(
                    painter = painter,
                    contentDescription = receta.nombreReceta,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                if (state is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Contenido de texto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Título de la receta
                Text(
                    text = receta.nombreReceta,
                    color = Color(0xFFAC5969), // Color rosa más oscuro
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                val ingredientesList = receta.ingredientes.split(".").map { it.trim() }.filter { it.isNotBlank() }

                Text(
                    buildAnnotatedString {
                        ingredientesList.forEach { ingrediente ->
                            append("•") // Agregar viñeta
                            append("  ") // Espaciado entre viñeta y texto
                            append(ingrediente)
                            append(".\n") // Salto de línea para cada ingrediente
                        }
                    },
                    color = Color(0xFFC07771),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Botones de acción
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                        .size(40.dp)
                        .background(Color(0xFFAD5D56), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Agregar a favoritos",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de detalles (tres puntos)
                IconButton(
                    onClick = {
                        navController.navigate("RecetasDetalle/${receta.idReceta}")
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color(0xFFAD5D56),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Ver detalles",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun imagenEmogi(emogi: String): Painter {
    return when (emogi) {
        "Feliz" -> painterResource(id = R.drawable.feli)
        "Triste" -> painterResource(id = R.drawable.tite)
        "Enojado" -> painterResource(id = R.drawable.enojao)
        else -> painterResource(id = R.drawable.feli)
    }
}

