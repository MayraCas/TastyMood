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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Recetas(
    navController: NavHostController,
    mood: String,
    diet: String,
    preferredIngredients: String,
    excludedIngredients: String
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEB)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 30.dp)
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Primer elemento alineado a la izquierda
                FloatingActionButton(
                    onClick = { navController.navigate("Home") },
                    contentColor = Color(0xFFAC5969),
                    containerColor = Color(0xFFFDC7BD),
                    modifier = Modifier.size(60.dp)
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

                // Segundo elemento centrado
                Text(
                    text = "Recetas",
                    color = Color(0xFFAC5969),
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(10.dp)
                )

                // Tercer elemento alineado a la derecha
                Box(
                    modifier = Modifier
                        .padding(15.dp)
                        .height(60.dp)
                        .width(65.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBEB))
                        .border(1.dp, Color(0xFFAC5969), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = imagenEmogi(mood),
                        contentDescription = "Estado de ánimo",
                        modifier = Modifier.size(65.dp)
                            .clip(RoundedCornerShape(60.dp))
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
        "Relajado" -> painterResource(id = R.drawable.relajao)
        "Triste" -> painterResource(id = R.drawable.tite)
        "Enojado" -> painterResource(id = R.drawable.enojao)
        "Estresado" -> painterResource(id = R.drawable.estresao)
        "Aburrido" -> painterResource(id = R.drawable.aburrio)
        else -> painterResource(id = R.drawable.feli)
    }
}