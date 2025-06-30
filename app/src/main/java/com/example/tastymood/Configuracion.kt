package com.example.tastymood

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.otroexample.model.UserDetails
import com.example.tastymood.utils.DataStoreManager
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun ConfigScreen (
    navController: NavHostController,
    dataStoreManager: DataStoreManager
) {
    val userDetails by dataStoreManager.getFromDataStore().collectAsState(initial = UserDetails())

    var nombre by remember { mutableStateOf("") }
    var generoSeleccionado by remember { mutableStateOf("Masculino") }
    var selectedDate by remember { mutableStateOf("") }
    val mContext = LocalContext.current
    val scope = rememberCoroutineScope()

    var isDateSelected by remember { mutableStateOf(false) }

    LaunchedEffect(userDetails.name) {
        nombre = userDetails.name
        generoSeleccionado = userDetails.gender
        selectedDate = userDetails.date
        isDateSelected = userDetails.date.isNotEmpty() // Marca como seleccionada si ya hay una fecha
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
            modifier = Modifier.padding(top = 30.dp)
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
                        navController.popBackStack()
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
                    text = "Configuración",
                    color = Color(0xFFAC5969),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Card(
                modifier = Modifier
                    .height(500.dp)
                    .padding(top = 5.dp, end = 15.dp, start = 15.dp, bottom = 5.dp)
                    .clip(RoundedCornerShape(50.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC6BB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Título
                    Text(
                        text = "Rellena los campos",
                        fontSize = 18.sp,
                        color = Color(0xFFAD5D56),
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                    )

                    // Campo Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text(text = "Nombre", color = Color(0xFFB76D7B)) },
                        placeholder = { Text(text = "Nombre", color = Color(0xFFD38D85)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(78.dp)
                            .padding(bottom = 15.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAC5969),
                            unfocusedBorderColor = Color(0xFFC07771),
                            cursorColor = Color(0xFFAC5969),
                            focusedTextColor = Color(0xFFAC5969),
                            unfocusedTextColor = Color(0xFFC07771)
                        )
                    )

                    // Campo Fecha de Nacimiento
                    val calendar = Calendar.getInstance()

                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    val datePickerDialog = DatePickerDialog(
                        mContext,
                        { _, selectedYear, selectedMonth, selectedDay ->
                            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                            isDateSelected = true // Marca como seleccionada la fecha
                        },
                        year, month, day
                    ).apply {
                        datePicker.maxDate = System.currentTimeMillis() // Restringir fechas futuras
                    }

                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        label = { Text(text = "Fecha de Nacimiento", color = Color(0xFFB76D7B)) },
                        placeholder = { Text("dd/mm/yyyy", color = Color(0xFFEAD1D6)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    tint = Color(0xFFB76D7B),
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAC5969),
                            unfocusedBorderColor = Color(0xFFC07771),
                            cursorColor = Color(0xFFAC5969),
                            focusedTextColor = Color(0xFFAC5969),
                            unfocusedTextColor = Color(0xFFC07771)
                        )
                    )

                    // Sección Género
                    Text(
                        text = "Género",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB76D7B),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Opción Masculino
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = generoSeleccionado == "Masculino",
                                    onClick = { generoSeleccionado = "Masculino" }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = generoSeleccionado == "Masculino",
                                onClick = { generoSeleccionado = "Masculino" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFAD5D56),
                                    unselectedColor = Color(0xFFC07771)
                                )
                            )
                            Text(
                                text = "Masculino",
                                modifier = Modifier.padding(start = 8.dp),
                                color = if (generoSeleccionado == "Masculino") Color(0xFFAD5D56) else Color(0xFFC07771)
                            )
                        }

                        // Opción Femenino
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = generoSeleccionado == "Femenino",
                                    onClick = { generoSeleccionado = "Femenino" }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = generoSeleccionado == "Femenino",
                                onClick = { generoSeleccionado = "Femenino" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFAD5D56),
                                    unselectedColor = Color(0xFFC07771)
                                )
                            )
                            Text(
                                text = "Femenino",
                                modifier = Modifier.padding(start = 8.dp),
                                color = if (generoSeleccionado == "Femenino") Color(0xFFAD5D56) else Color(0xFFC07771)
                            )
                        }
                    }

                    // Botón Hecho
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FloatingActionButton(
                            onClick = {
                                // Acción al hacer clic
                                if (nombre.trim().isEmpty()) {
                                    Toast.makeText(
                                        mContext,
                                        "El nombre está vacío",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (selectedDate.isEmpty()) {
                                    Toast.makeText(
                                        mContext,
                                        "No ha seleccionado su fecha de nacimiento",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    scope.launch {
                                        dataStoreManager.saveToDateStore(
                                            UserDetails(
                                                name = nombre.trim(),
                                                date = selectedDate,
                                                gender = generoSeleccionado
                                            )
                                        )
                                        navController.navigate("Home") {
                                            popUpTo("Home") { inclusive = true }
                                        }
                                    }
                                }
                            },
                            containerColor = Color(0xFFAD5D56),
                            contentColor = Color.White,
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                modifier = Modifier.size(40.dp),
                                contentDescription = "Hecho"
                            )
                        }
                    }
                }
            }
        }
    }

}
