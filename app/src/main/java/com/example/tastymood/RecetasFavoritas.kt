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
import java.nio.file.WatchEvent


@Composable
fun RecetasFavoritas(
    navController: NavHostController
){

    val recetaViewModel: RecetaViewModel = viewModel ()
    val database = recetaViewModel.database

    var recetas by remember { mutableStateOf<List<Receta>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        try {
            recetas = database.favoritoDao().getRecetasFavoritas()
            Log.d("Recetas Favoritas", "Recetas cargadas: $recetas")
        } catch (e: Exception) {
            Log.e("Recetas Favoritas", "Error al cargar las recetas favoritas", e)
            errorMessage = "Error al cargar las recetas favoritas"
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
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(50.dp)
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
                    text = "Favoritos",
                    color = Color(0xFFAC5969),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )
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
                                text = "No se encontraron recetas favoritas",
                                color = Color(0xFFAC5969),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Para poder ver sus recetas favoritas primero debe marcarlas como favoritas",
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
                            RecetaFavCard(
                                navController = navController,
                                database = database,
                                receta = receta,
                                onRemoveFavorito = { idReceta ->
                                    recetas = recetas.filter { it.idReceta != idReceta }
                                }
                            )
                        }

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
fun RecetaFavCard(
    navController: NavHostController,
    database: AppDatabase,
    receta: Receta,
    onRemoveFavorito: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDC7BD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = receta.nombreReceta,
                    color = Color(0xFFAC5969),
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                val ingredientesList = receta.ingredientes.split(".").map { it.trim() }.filter { it.isNotBlank() }
                Text(
                    buildAnnotatedString {
                        ingredientesList.forEach { ingrediente ->
                            append("•  ")
                            append(ingrediente)
                            append(".\n")
                        }
                    },
                    color = Color(0xFFC07771),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            database.favoritoDao().deleteFavorito(receta.idReceta)
                            onRemoveFavorito(receta.idReceta)
                        }
                    },
                    modifier = Modifier.size(40.dp).background(Color(0xFFAD5D56), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Agregar a favoritos",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

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


