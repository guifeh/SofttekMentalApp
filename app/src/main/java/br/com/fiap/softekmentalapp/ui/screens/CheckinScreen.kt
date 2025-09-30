package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.fiap.softekmentalapp.viewmodel.CheckinState
import br.com.fiap.softekmentalapp.viewmodel.CheckinViewModel
import br.com.fiap.softekmentalapp.model.CheckinRequest

@Composable
fun CheckinScreen(
    token: String,
    navController: NavHostController,
    checkinViewModel: CheckinViewModel = viewModel()
) {
    var emotion by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val state by checkinViewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Novo Check-in", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emotion,
            onValueChange = { emotion = it },
            label = { Text("Como você está se sentindo?") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Observações (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val request = CheckinRequest(emotion = emotion, note = note)
                checkinViewModel.createCheckin(token, emotion, note)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Check-in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val s = state) {
            is CheckinState.Loading -> CircularProgressIndicator()
            is CheckinState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate("feedback/${s.response.emotion}")
                    checkinViewModel.resetState()
                }
            }
            is CheckinState.Error -> Text("Erro: ${s.message}", color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}
