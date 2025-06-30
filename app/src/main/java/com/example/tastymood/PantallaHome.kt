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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tastymood.database.AppDatabase
import com.example.tastymood.utils.DataStoreManager

data class BotonData(val label: String, val imageRes: Int, val descripcion: String)

@Composable
fun PantallaHome (
    navController: NavHostController,
    dataStoreManager: DataStoreManager
) {

    val userDetails by dataStoreManager.getFromDataStore().collectAsState(initial = null)
    var preferredIngredients by rememberSaveable { mutableStateOf("") }
    var excludedIngredients by rememberSaveable { mutableStateOf("") }

    val botonesEmogis = listOf(
        BotonData("Feliz", R.drawable.feli, "Feliz"),
        BotonData("Triste", R.drawable.tite, "Triste"),
        BotonData("Enojado", R.drawable.enojao, "Enojado")
    )
    var emogisSelect by rememberSaveable {mutableStateOf(botonesEmogis.first().label)}

    val opcionesDieta = listOf(
        "Ninguna",
        "En vegetales",
        "En animales",
        "Sin azúcares"
    )
    var dietaSelect by rememberSaveable {mutableStateOf(opcionesDieta.first())}

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFFFEBEB)),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("RecetasFavoritas")
                    },
                    contentColor = Color(0xFFAC5969),
                    containerColor = Color(0xFFFDC7BD),
                    modifier = Modifier.size(70.dp).padding(10.dp),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favoritos",
                        tint = Color(0xFFAC5969),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    text = "¿Cómo te sientes hoy${userDetails?.name?.takeIf { it.isNotEmpty() }?.let { ", $it" } ?: ""}?",
                    color = Color(0xFFAC5969),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    maxLines = 2,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                )

                FloatingActionButton(
                    onClick = {
                        navController.navigate("Configuracion")
                    },
                    contentColor = Color(0xFFAC5969),
                    containerColor = Color(0xFFFDC7BD),
                    modifier = Modifier.size(70.dp).padding(10.dp),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración",
                        tint = Color(0xFFAC5969),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Text(
                text = "¡Tus emociones son importantes para saber qué podrías comer hoy!",
                color = Color(0xFFAC5969),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )

            botonesEmogis.chunked(3).forEach { fila ->
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    fila.forEach { boton ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 2.dp, end = 2.dp)
                                    .height(70.dp)
                                    .width(75.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEBEB))
                                    .border( if (emogisSelect == boton.label) 3.dp else 2.dp, Color(0xFFAC5969), CircleShape)
                                    .clickable { emogisSelect = boton.label },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = boton.imageRes),
                                    contentDescription = boton.label,
                                    modifier = Modifier.size(75.dp)
                                        .clip(RoundedCornerShape(60.dp))
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = boton.descripcion,
                                color = if (emogisSelect == boton.label) Color(0xFFAC5969) else Color(0xFFB76D7B),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .height(550.dp)
                    .padding(top = 5.dp, end = 15.dp, start = 15.dp, bottom = 5.dp)
                    .clip(RoundedCornerShape(50.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC6BB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    // Título de la sección de dieta
                    Text(
                        text = "¿Posees alguna dieta o estilo de vida?",
                        fontSize = 15.sp,
                        color = Color(0xFF8B5A5A),
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        opcionesDieta.chunked(2).forEach { fila ->
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth(),
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                fila.forEach { label ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        RadioButton(
                                            selected = dietaSelect == label,
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFFAD5D56),
                                                unselectedColor = Color(0xFFC07771)
                                            ),
                                            onClick = { dietaSelect = label }
                                        )
                                        Text(text = label,
                                            fontSize = 14.sp,
                                            color = if (dietaSelect == label) Color(0xFFAD5D56) else Color(0xFFC07771)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = "¿Algún ingrediente que prefieras hoy?",
                            fontSize = 15.sp,
                            color = Color(0xFF8B5A5A)
                        )

                        OutlinedTextField(
                            value = preferredIngredients,
                            onValueChange = { preferredIngredients = it },
                            placeholder = {
                                Text(
                                    text = "Ingrediente",
                                    color = Color(0xFFC07771)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAC5969),
                                unfocusedBorderColor = Color(0xFFC07771),
                                cursorColor = Color(0xFFAC5969),
                                focusedTextColor = Color(0xFFAC5969),
                                unfocusedTextColor = Color(0xFFC07771)
                            )
                        )
                    }

                    // Campo de ingredientes a excluir
                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = "¿Algún ingrediente que desees excluir hoy?",
                            fontSize = 15.sp,
                            color = Color(0xFF8B5A5A)
                        )

                        OutlinedTextField(
                            value = excludedIngredients,
                            onValueChange = { excludedIngredients = it },
                            placeholder = {
                                Text(
                                    text = "Ingrediente",
                                    color = Color(0xFFC07771)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAC5969),
                                unfocusedBorderColor = Color(0xFFC07771),
                                cursorColor = Color(0xFFAC5969),
                                focusedTextColor = Color(0xFFAC5969),
                                unfocusedTextColor = Color(0xFFC07771)
                            )
                        )
                    }

                    // Botón de confirmación
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("Recetas/$emogisSelect/$dietaSelect/$preferredIngredients/$excludedIngredients")
                            },
                            containerColor = Color(0xFFAD5D56),
                            contentColor = Color.White,
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirmar",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

            }
        }
    }

}
@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFEBEB)
fun PreviewPantallaHome() {
    PantallaHome(
        navController = NavHostController(LocalContext.current),
        dataStoreManager = DataStoreManager(LocalContext.current)
    )
}