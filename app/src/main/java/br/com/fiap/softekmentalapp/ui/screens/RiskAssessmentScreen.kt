package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.softekmentalapp.model.AssessmentRequest
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

    var showResult by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Avaliação Psicossocial") }) }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(questions.size) { index ->
                val question = questions[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = question.text, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            (1..5).forEach { value ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    RadioButton(
                                        selected = responses[question.id] == value,
                                        onClick = { responses[question.id] = value }
                                    )
                                    Text(text = "$value")
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (responses.size == questions.size) {
                            val requests = questions.mapNotNull { q ->
                                val ans = responses[q.id]
                                if (ans != null) AssessmentRequest(question = q.text, answer = ans)
                                else null
                            }
                            assessmentViewModel.submitBatch(token, requests)
                        } else {
                            resultMessage = "Responda todas as perguntas antes de enviar."
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
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

            item {
                Spacer(modifier = Modifier.height(16.dp))

                when (val s = state) {
                    is AssessmentState.Success -> {
                        val data = s.data
                        val count = if (data is List<*>) (data as List<*>).size else -1
                        Text("Avaliações enviadas: $count", color = MaterialTheme.colorScheme.primary)
                    }
                    is AssessmentState.Error -> {
                        Text("Erro: ${s.message}", color = MaterialTheme.colorScheme.error)
                    }
                    else -> {}
                }
            }
        }
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            confirmButton = {
                TextButton(onClick = { showResult = false }) {
                    Text("OK")
                }
            },
            title = { Text("Aviso") },
            text = { Text(resultMessage) }
        )
    }
}
