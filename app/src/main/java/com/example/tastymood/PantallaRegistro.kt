package com.example.tastymood

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.otroexample.model.UserDetails
import com.example.tastymood.utils.DataStoreManager
import kotlinx.coroutines.launch
import com.example.tastymood.ui.theme.TastyMoodTheme
import com.example.tastymood.utils.preferenceDataStore
import kotlinx.coroutines.flow.first
import java.util.Calendar
import com.example.tastymood.utils.DataStoreManager.Companion.NAME


class PantallaRegistro : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            TastyMoodTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    val dataStoreContext = LocalContext.current
                    val dataStoreManger = DataStoreManager(dataStoreContext)

                    AppContent(
                        preferenceDataStore,
                        dataStoreManger
                    )

                }
            }
        }
    }
}

@Composable
fun AppContent(
    preferenceDataStore: DataStore<Preferences>,
    dataStoreManger: DataStoreManager
) { var isRegistered by remember { mutableStateOf(false)}
    val onRegisterSuccess = { isRegistered = true }
    LaunchedEffect(key1 = Unit) {
        checkRegisterState(preferenceDataStore) { it ->
            isRegistered = it
        }
    }
    if (isRegistered) {
        Navegacion(dataStoreManger)
    } else {
        RegisterPage(onRegisterSuccess, dataStoreManger)
    }
}

@Composable
fun RegisterPage(
    onRegisterSuccess: () -> Unit,
    dataStoreManager: DataStoreManager
) {
    var nombre by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var generoSeleccionado by remember { mutableStateOf("Masculino") }
    val mContext = LocalContext.current
    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEDADA))
    ) {
        // Sección superior con ilustración
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 0.dp),
            contentAlignment = Alignment.Center
        ) {
            // Ilustración principal
            IlustracionPrincipal()
        }

        // Sección inferior con formulario
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(top = 0.dp, bottom = 0.dp)
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        fontSize = 30.sp,
                        color = Color(0xFFAD5D56),
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                    )

                    // Campo Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text(text = "Nombre", color = Color(0xFFB76D7B) ) },
                        placeholder = { Text(text = "Nombre", color = Color(0xFFD38D85)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD38D85),
                            unfocusedBorderColor = Color(0xFFEAD1D6)
                        )
                    )

                    // Campo Fecha de Nacimiento
                    val context = LocalContext.current
                    val calendar = Calendar.getInstance()

                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    var selectedDate by remember { mutableStateOf("") }
                    var isDateSelected by remember { mutableStateOf(false) } // Variable de validación

                    val datePickerDialog = DatePickerDialog(
                        context,
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
                        placeholder = {Text("dd/mm/yyyy", color = Color(0xFFEAD1D6)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(imageVector = Icons.Default.DateRange,
                                    tint = Color(0xFFB76D7B),
                                    contentDescription = "Seleccionar fecha")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD38D85),
                            unfocusedBorderColor = Color(0xFFEAD1D6)
                        )
                    )

                    // Sección Género
                    Text(
                        text = "Género",
                        fontSize = 16.sp,
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
                                    selectedColor = Color(0xFFD38D85),
                                    unselectedColor = Color(0xFFEAD1D6)
                                )
                            )
                            Text(
                                text = "Masculino",
                                modifier = Modifier.padding(start = 8.dp),
                                color = if(generoSeleccionado == "Masculino") Color(0xFFD38D85) else Color(0xFFEAD1D6)
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
                                    selectedColor = Color(0xFFD38D85),
                                    unselectedColor = Color(0xFFEAD1D6)
                                )
                            )
                            Text(
                                text = "Femenino",
                                modifier = Modifier.padding(start = 8.dp),
                                color = if(generoSeleccionado == "Femenino") Color(0xFFD38D85) else Color(0xFFEAD1D6)
                            )
                        }
                    }

                    // Botón Continuar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FloatingActionButton(
                            onClick = {
                                // Acción al hacer clic
                                if (nombre.isEmpty()) {
                                    Toast.makeText(mContext, "El nombre está vacío", Toast.LENGTH_SHORT).show()
                                } else if (!isDateSelected) {
                                    Toast.makeText(mContext, "No ha seleccionado su fecha de nacimiento", Toast.LENGTH_SHORT).show()
                                } else {
                                    scope.launch {
                                        dataStoreManager.saveToDateStore(
                                            UserDetails(
                                                name = nombre,
                                                date = selectedDate,
                                                gender = generoSeleccionado
                                            )
                                        )
                                        onRegisterSuccess()
                                    }
                                }
                            },
                            containerColor = Color(0xFFAD5D56),
                            contentColor = Color.White,
                            modifier = Modifier.size(100.dp),
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                modifier = Modifier.size(50.dp),
                                contentDescription = "Continuar"
                            )
                        }
                    }
                }
        }
    }
}


@Composable
fun IlustracionPrincipal() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tazas_ilustracion),
            contentDescription = "Ilustración de chocolate caliente",
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(top = 20.dp, bottom = 0.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            contentScale = ContentScale.FillWidth
        )
    }
}

suspend fun checkRegisterState(
    preferenceDataStore: DataStore<Preferences>,
    onResult: (Boolean) -> Unit
) {
    val preferences = preferenceDataStore.data.first()
    val email = preferences[NAME]
    val isRegistered = email != null
    onResult(isRegistered)
}