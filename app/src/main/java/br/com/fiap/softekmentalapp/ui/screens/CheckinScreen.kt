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


@Composable
fun CheckinScreen(navController: NavController) {
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
            CheckinRepository.addCheckin(Checkin("feliz"))
            navController.navigate(AppScreen.Feedback.createRoute("feliz"))
        }) {
            Text("Feliz")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            CheckinRepository.addCheckin(Checkin("triste"))
            navController.navigate(AppScreen.Feedback.createRoute("triste"))
        }) {
            Text("Triste")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            CheckinRepository.addCheckin(Checkin("ansioso"))
            navController.navigate(AppScreen.Feedback.createRoute("ansioso"))
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
