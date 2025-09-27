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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.fiap.softekmentalapp.model.Checkin
import br.com.fiap.softekmentalapp.repository.CheckinRepository

@Composable
fun FeedbackScreen(
    emotion: String,
    navController: NavController,
    repository: CheckinRepository
) {
    val message = when (emotion.lowercase()) {
        "feliz" -> "Que bom saber que você está bem! 😊"
        "triste" -> "Lembre-se: tudo passa. Estamos com você. 💙"
        "ansioso" -> "Respire fundo... vai ficar tudo bem. 🍃"
        else -> "Sentimento desconhecido."
    }

    var allCheckins by remember { mutableStateOf<List<Checkin>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            allCheckins = repository.getAllCheckins() // suspensa, então LaunchedEffect é o lugar certo
            allCheckins.forEach { checkin ->
                Log.d("Checkins", "Checkin: ${checkin.emotion} - ${checkin.timestamp}")
            }
        } catch (e: Exception) {
            Log.e("FeedbackScreen", "Erro ao buscar checkins: ${e.message}")
        }
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


