package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import br.com.fiap.softekmentalapp.repository.CheckinRepository

@Composable
fun FeedbackScreen(emotion: String, navController: NavController) {
    val message = when (emotion.lowercase()) {
        "feliz" -> "Que bom saber que você está bem! 😊"
        "triste" -> "Lembre-se: tudo passa. Estamos com você. 💙"
        "ansioso" -> "Respire fundo... vai ficar tudo bem. 🍃"
        else -> "Sentimento desconhecido."
    }

    LaunchedEffect(Unit) {
        Log.d("Checkins", "Todos os check-ins: ${CheckinRepository.getAllCheckins()}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}

