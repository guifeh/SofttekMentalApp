package br.com.fiap.softekmentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.softekmentalapp.model.AssessmentResult
import br.com.fiap.softekmentalapp.repository.AssessmentRepository

data class Question(val id: Int, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiskAssessmentScreen(navController: NavController) {
    val questions = listOf(
        Question(1, "Com que frequência você se sente sobrecarregado(a) com suas tarefas?"),
        Question(2, "Você sente que tem apoio suficiente no trabalho?"),
        Question(3, "Você tem dificuldades para dormir por conta de preocupações com o trabalho?")
    )

    // Armazena a resposta selecionada para cada pergunta (1 a 5)
    val responses = remember { mutableStateMapOf<Int, Int>() }

    var showResult by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            confirmButton = {
                TextButton(onClick = { showResult = false }) {
                    Text("OK")
                }
            },
            title = { Text("Resultado da Avaliação") },
            text = { Text(resultText) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Avaliação Psicossocial") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            questions.forEach { question ->
                item {
                    Text(
                        text = question.text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        (1..5).forEach { value ->
                            Row(
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
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

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (responses.size == questions.size) {
                            val totalScore = responses.values.sum()
                            val average = totalScore / questions.size.toDouble()
                            val classification = when {
                                average <= 2 -> "Risco Baixo. Continue cuidando da sua saúde mental!"
                                average <= 3.5 -> "Risco Moderado. Fique atento e busque apoio se necessário."
                                else -> "Risco Alto. Recomendamos procurar ajuda especializada."
                            }

                            // Salvar resultado no repositório
                            AssessmentRepository.addResult(
                                AssessmentResult(
                                    timestamp = System.currentTimeMillis(),
                                    responses = responses.toMap(),
                                    score = average,
                                    classification = classification
                                )
                            )

                            resultText = classification
                            showResult = true
                        } else {
                            resultText = "Por favor, responda todas as perguntas."
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar Avaliação")
                }
            }
        }
    }
}