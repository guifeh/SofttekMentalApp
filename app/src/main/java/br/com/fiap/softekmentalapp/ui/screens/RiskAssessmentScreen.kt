package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
            TopAppBar(
                title = {
                    Text(
                        "Avaliação Psicossocial",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                tonalElevation = 0.dp
            ) {
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
                        .padding(24.dp)
                        .height(56.dp),
                    enabled = state !is AssessmentState.Loading,
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (state is AssessmentState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Enviando...", style = MaterialTheme.typography.labelLarge)
                    } else {
                        Text("Enviar Avaliação", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
                start = 20.dp,
                end = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        "Responda honestamente sobre como você tem se sentido nos últimos dias:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(questions.size) { index ->
                val question = questions[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Text(
                                text = question.text,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outlineVariant)

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            scaleLabels.forEachIndexed { i, label ->
                                val value = i + 1
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    RadioButton(
                                        selected = responses[question.id] == value,
                                        onClick = { responses[question.id] = value }
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

}
