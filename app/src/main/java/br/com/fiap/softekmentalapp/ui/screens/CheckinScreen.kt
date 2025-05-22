package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.navigation.AppScreen
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.model.Checkin
import br.com.fiap.softekmentalapp.model.Emotion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CheckinScreen(
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Como você está se sentindo hoje?", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            coroutineScope.launch {
                CheckinRepository.addCheckin(Checkin(emotion = Emotion.Happy.id.toString()))
                navController.navigate(AppScreen.Feedback.createRoute(Emotion.Happy.routeParam))
            }
        }) {
            Text("Feliz")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            coroutineScope.launch {
                CheckinRepository.addCheckin(Checkin(emotion = Emotion.Sad.id.toString()))
                navController.navigate(AppScreen.Feedback.createRoute(Emotion.Sad.routeParam))
            }
        }) {
            Text("Triste")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            coroutineScope.launch {
                CheckinRepository.addCheckin(Checkin(emotion = Emotion.Anxious.id.toString()))
                navController.navigate(AppScreen.Feedback.createRoute(Emotion.Anxious.routeParam))
            }
        }) {
            Text("Ansioso")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            navController.navigate(AppScreen.History.route)
        }) {
            Text("Ver Histórico")
        }
    }
}