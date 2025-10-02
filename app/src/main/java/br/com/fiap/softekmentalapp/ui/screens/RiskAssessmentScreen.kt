package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.fiap.softekmentalapp.model.AssessmentRequest
import br.com.fiap.softekmentalapp.model.AssessmentSummaryResponse
import br.com.fiap.softekmentalapp.viewmodel.AssessmentState
import br.com.fiap.softekmentalapp.viewmodel.AssessmentViewModel

data class Question(val id: Int, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiskAssessmentScreen(
    token: String,
    navController: NavHostController,
    assessmentViewModel: AssessmentViewModel = viewModel()
) {
    val questions = assessmentViewModel.getQuestions()
    val responses = remember { mutableStateMapOf<Int, Int>() }
    val state by assessmentViewModel.state.collectAsState()

    var showWarning by remember { mutableStateOf(false) }
    var warningMessage by remember { mutableStateOf("") }

    val scaleLabels = listOf("Nada", "Pouco", "Moderado", "Muito", "Extremo")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Avaliação Psicossocial") })
        },
        bottomBar = {
            Button(
                onClick = {
                    if (responses.size == questions.size) {
                        val requests = questions.mapNotNull { q ->
                            responses[q.id]?.let { AssessmentRequest(question = q.text, answer = it) }
                        }
                        assessmentViewModel.submitBatch(token, requests)
                    } else {
                        warningMessage = "Responda todas as perguntas antes de enviar."
                        showWarning = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = state !is AssessmentState.Loading
            ) {
                if (state is AssessmentState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Enviando...")
                } else {
                    Text("Enviar Avaliação")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    "Responda honestamente sobre como você tem se sentido nos últimos dias:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(questions.size) { index ->
                val question = questions[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = question.text,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            scaleLabels.forEachIndexed { i, label ->
                                val value = i + 1
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    RadioButton(
                                        selected = responses[question.id] == value,
                                        onClick = { responses[question.id] = value }
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showWarning) {
        AlertDialog(
            onDismissRequest = { showWarning = false },
            confirmButton = {
                TextButton(onClick = { showWarning = false }) {
                    Text("OK")
                }
            },
            title = { Text("Aviso") },
            text = { Text(warningMessage) }
        )
    }

    if (state is AssessmentState.Success) {
        val summary = (state as AssessmentState.Success).data as? AssessmentSummaryResponse
        if (summary != null) {
            AlertDialog(
                onDismissRequest = { assessmentViewModel.resetState() },
                confirmButton = {
                    TextButton(onClick = { assessmentViewModel.resetState() }) {
                        Text("OK")
                    }
                },
                title = { Text("Resultado da Avaliação") },
                text = {
                    Column {

                        Spacer(modifier = Modifier.height(8.dp))

                        val (statusColor, statusEmoji) = when (summary.status) {
                            "Estável" -> MaterialTheme.colorScheme.primary to "Parabéns! Continue assim! ✅"
                            "Atenção" -> MaterialTheme.colorScheme.tertiary to "Tenha atenção! Cuidado com o ambiente ⚠️"
                            "Crítico" -> MaterialTheme.colorScheme.error to "Estado crítico, procure ajuda! ❌"
                            else -> MaterialTheme.colorScheme.onSurface to "ℹ️"
                        }

                        Text(
                            text = "$statusEmoji Status: ${summary.status}",
                            color = statusColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    }

    if (state is AssessmentState.Error) {
        val error = (state as AssessmentState.Error).message
        AlertDialog(
            onDismissRequest = { assessmentViewModel.resetState() },
            confirmButton = {
                TextButton(onClick = { assessmentViewModel.resetState() }) {
                    Text("OK")
                }
            },
            title = { Text("Erro") },
            text = { Text(error) }
        )
    }
}
