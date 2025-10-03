package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.fiap.softekmentalapp.viewmodel.CheckinState
import br.com.fiap.softekmentalapp.viewmodel.CheckinViewModel
import br.com.fiap.softekmentalapp.model.CheckinRequest
import kotlinx.coroutines.launch

@Composable
fun CheckinScreen(
    token: String,
    navController: NavHostController,
    checkinViewModel: CheckinViewModel = viewModel()
) {
    var emotion by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    val state by checkinViewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Novo Check-in",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Como você está se sentindo hoje?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = emotion,
                    onValueChange = { emotion = it },
                    label = { Text("Sua emoção") },
                    placeholder = { Text("Ex: Feliz, Triste, Ansioso...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Observações (opcional)") },
                    placeholder = { Text("Adicione detalhes sobre como você se sente") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        checkinViewModel.createCheckin(token, emotion, note)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    enabled = emotion.isNotBlank()
                ) {
                    Text(
                        text = "Registrar Check-in",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                when (val s = state) {
                    is CheckinState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is CheckinState.Success -> {
                        LaunchedEffect(s) {
                            feedbackMessage = when (s.response.emotion.lowercase()) {
                                "feliz" -> "Que bom saber que você está bem! 😊"
                                "triste" -> "Lembre-se: tudo passa. Estamos com você 💙"
                                "ansioso" -> "Respire fundo... vai ficar tudo bem 🍃"
                                else -> "Sentimento registrado com sucesso ✅"
                            }
                            showDialog = true
                            checkinViewModel.resetState()
                        }
                    }
                    is CheckinState.Error -> {
                        Text(
                            text = "Erro: ${s.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK")
                }
            },
            title = {
                Text(
                    text = "Feedback",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    text = feedbackMessage,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            shape = MaterialTheme.shapes.large
        )
    }
}
