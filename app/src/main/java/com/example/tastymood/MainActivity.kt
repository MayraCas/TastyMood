package com.example.tastymood

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tastymood.ui.theme.TastyMoodTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TastyMoodTheme {
                Column ( modifier = Modifier.Companion.fillMaxSize(),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tazas_ilustracion),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .height(200.dp)
                            .clip(RoundedCornerShape(
                                topStart = 60.dp,
                                topEnd = 60.dp,
                                bottomStart = 60.dp,
                                bottomEnd = 60.dp)
                            ),
                        contentScale = ContentScale.FillHeight
                    )

                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color(0xFFAD5D56),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.Companion.padding(top = 16.dp)
                    )

                    CircularProgressIndicator(
                        modifier = Modifier.Companion.padding(top = 16.dp),
                        color = Color(0xFFFFC6BB)
                    )
                }
                LaunchedEffect(key1 = true) {
                    delay(3000L)
                    startActivity(Intent(this@MainActivity, PantallaRegistro::class.java))
                    finish()
                }
            }
        }
    }
}
